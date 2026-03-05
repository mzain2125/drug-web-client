package Unsc.web.client.service;

import Unsc.web.client.entity.UnscData;
import Unsc.web.client.exception.XmlFetchException;
import Unsc.web.client.exception.XmlParseException;
import Unsc.web.client.repository.UnscDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UnscService {

    private static final String ENTITY_TYPE_INDIVIDUAL = "I";
    private static final String ENTITY_TYPE_ENTITY = "E";
    private static final int DATE_PREFIX_LENGTH = 10; // "YYYY-MM-DD"

    @Value("${unsc.url}")
    private String unscUrl;

    private final WebClient webClient;
    private final UnscDataRepository unscDataRepository;
    private final XmlMapper xmlMapper;

    public UnscService(WebClient webClient, UnscDataRepository unscDataRepository) {
        this.webClient = webClient;
        this.unscDataRepository = unscDataRepository;
        this.xmlMapper = new XmlMapper();
    }

    @PostConstruct
    public void runOnStartup() {
        log.info("Application started. Running UNSC data fetch automatically...");
        fetchAndSave();
    }

    @Transactional
    public String fetchAndSave() {

        log.info("Deleting existing UNSC data.");
        unscDataRepository.deleteAll();

        log.info("Starting UNSC data fetch from URL: {}", unscUrl);
        String xml = fetchXml();
        JsonNode root = parseXml(xml);

        // Extract both Individuals and Entities
        List<UnscData> allEntities = extractAndMapAll(root);

        if (allEntities.isEmpty()) {
            log.warn("No valid records found. Nothing saved to the database.");
            return "No records were saved. The XML may be empty or the structure has changed.";
        }

        unscDataRepository.saveAll(allEntities);
        log.info("SUCCESS: {} records inserted into UNSC_DATA table.", allEntities.size());

        return String.format("Success: %d records saved to the database.", allEntities.size());
    }

    private String fetchXml() {
        log.debug("Sending GET request to: {}", unscUrl);
        try {
            String xml = webClient.get()
                    .uri(unscUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (xml == null || xml.isBlank()) {
                throw new XmlFetchException("Remote endpoint returned empty response from URL: " + unscUrl);
            }

            log.debug("Received XML response, length={} characters", xml.length());
            return xml;

        } catch (WebClientResponseException e) {
            log.error("HTTP error while fetching XML. Status={}, Body={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new XmlFetchException("HTTP " + e.getStatusCode() + " received from UNSC endpoint", e);
        } catch (XmlFetchException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during XML fetch from URL: {}", unscUrl, e);
            throw new XmlFetchException("Failed to fetch XML from: " + unscUrl, e);
        }
    }

    private JsonNode parseXml(String xml) {
        log.debug("Parsing XML into JsonNode tree");
        try {
            return xmlMapper.readTree(xml);
        } catch (Exception e) {
            log.error("Failed to parse XML. Snippet: {}",
                    xml.length() > 200 ? xml.substring(0, 200) + "..." : xml, e);
            throw new XmlParseException("XML parsing failed", e);
        }
    }

    private List<UnscData> extractAndMapAll(JsonNode root) {
        List<UnscData> entities = new ArrayList<>();

        // ---------- INDIVIDUALS ----------
        JsonNode individuals = root.path("INDIVIDUALS").path("INDIVIDUAL");
        if (individuals.isArray()) {
            for (JsonNode person : individuals) {
                try {
                    UnscData entity = mapToEntity(person, ENTITY_TYPE_INDIVIDUAL);
                    entities.add(entity);
                } catch (Exception e) {
                    log.warn("Skipping INDIVIDUAL DATAID={} due to mapping error: {}",
                            person.path("DATAID").asLong(-1), e.getMessage());
                }
            }
        }

        // ---------- ENTITIES / GROUPS ----------
        JsonNode groups = root.path("ENTITIES").path("ENTITY");
        if (groups.isArray()) {
            for (JsonNode group : groups) {
                try {
                    UnscData entity = mapToEntity(group, ENTITY_TYPE_ENTITY);
                    entities.add(entity);
                } catch (Exception e) {
                    log.warn("Skipping ENTITY DATAID={} due to mapping error: {}",
                            group.path("DATAID").asLong(-1), e.getMessage());
                }
            }
        }

        log.info("Total records mapped: {} (Individuals + Entities)", entities.size());
        return entities;
    }

    private UnscData mapToEntity(JsonNode node, String type) {
        UnscData entity = new UnscData();
        entity.setDataId(node.path("DATAID").asLong());
        entity.setVersionNum(node.path("VERSIONNUM").asInt());
        entity.setFirstName(nullIfBlank(node.path("FIRST_NAME").asText(null)));
        entity.setSecondName(nullIfBlank(node.path("SECOND_NAME").asText(null)));
        entity.setThirdName(nullIfBlank(node.path("THIRD_NAME").asText(null)));
        entity.setUnListType(nullIfBlank(node.path("UN_LIST_TYPE").asText(null)));
        entity.setReferenceNumber(nullIfBlank(node.path("REFERENCE_NUMBER").asText(null)));
        entity.setListedOn(parseDate(node.path("LISTED_ON").asText(null), node.path("DATAID").asLong()));
        entity.setNameOriginalScript(nullIfBlank(node.path("NAME_ORIGINAL_SCRIPT").asText(null)));
        entity.setComments1(nullIfBlank(node.path("COMMENTS1").asText(null)));
        entity.setType(type);
        return entity;
    }

    private LocalDate parseDate(String raw, long dataid) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String datePart = raw.length() >= DATE_PREFIX_LENGTH
                ? raw.substring(0, DATE_PREFIX_LENGTH)
                : raw;
        try {
            return LocalDate.parse(datePart);
        } catch (DateTimeParseException e) {
            log.warn("Could not parse date '{}' for DATAID={}; storing null.", raw, dataid);
            return null;
        }
    }

    private String nullIfBlank(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}