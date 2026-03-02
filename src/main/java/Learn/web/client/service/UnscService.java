package Learn.web.client.service;

import Learn.web.client.entity.UnscData;
import Learn.web.client.exception.XmlFetchException;
import Learn.web.client.exception.XmlParseException;
import Learn.web.client.repository.UnscDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

/**
 * Service responsible for fetching UNSC Individual sanctions data from the
 * UN XML feed, parsing it, and persisting relevant fields to the database.
 */
@Slf4j
@Service
public class UnscService {

    private static final String ENTITY_TYPE_INDIVIDUAL = "I";
    private static final int DATE_PREFIX_LENGTH = 10; // "YYYY-MM-DD"
//    private static final Logger log = LoggerFactory.getLogger(UnscService.class);
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
    /**
     * Fetches the UNSC XML feed, maps Individual records directly to entities,
     * saves them to the database, and returns a success message.
     *
     * @return success message with inserted count
     */
    @Transactional
    public String fetchAndSave() {

        log.info("Deleting UNSC Individual data.");
        unscDataRepository.deleteAll();
        log.info("Starting UNSC Individual data fetch from URL: {}", unscUrl);
        String xml = fetchXml();
        JsonNode root = parseXml(xml);
        List<UnscData> entities = extractAndMapIndividuals(root);

        if (entities.isEmpty()) {
            log.warn("No valid individual records found. Nothing saved to the database.");
            return "No records were saved. The XML may be empty or the structure has changed.";
        }

        unscDataRepository.saveAll(entities);
        log.info("SUCCESS: {} individual records inserted into UNSC_DATA table.", entities.size());

        return String.format("Success: %d individual records saved to the database.", entities.size());
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
            throw e; // re-throw our own exception as-is
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

    /**
     * Walks the XML tree and maps each INDIVIDUAL node directly to an UnscData entity.
     * Records that fail mapping are skipped and logged individually.
     */
    private List<UnscData> extractAndMapIndividuals(JsonNode root) {
        JsonNode individuals = root.path("INDIVIDUALS").path("INDIVIDUAL");

        if (!individuals.isArray()) {
            log.warn("No INDIVIDUAL array found under INDIVIDUALS node. XML structure may have changed.");
            return List.of();
        }

        log.debug("Found {} INDIVIDUAL nodes in XML", individuals.size());

        List<UnscData> entities = new ArrayList<>(individuals.size());
        int skipped = 0;

        for (JsonNode person : individuals) {
            long dataid = person.path("DATAID").asLong(-1);
            try {
                entities.add(mapToEntity(person));
            } catch (Exception e) {
                log.warn("Skipping DATAID={} due to mapping error: {}", dataid, e.getMessage());
                skipped++;
            }
        }

        if (skipped > 0) {
            log.warn("Skipped {} individual records due to mapping errors.", skipped);
        }

        log.info("Mapped {} valid entities from {} total XML records ({} skipped).",
                entities.size(), individuals.size(), skipped);

        return entities;
    }

    /**
     * Maps a single XML INDIVIDUAL JsonNode directly to an UnscData entity.
     */
    private UnscData mapToEntity(JsonNode person) {
        UnscData entity = new UnscData();
        entity.setDataId(person.path("DATAID").asLong());
        entity.setVersionNum(person.path("VERSIONNUM").asInt());
        entity.setFirstName(nullIfBlank(person.path("FIRST_NAME").asText(null)));
        entity.setSecondName(nullIfBlank(person.path("SECOND_NAME").asText(null)));
        entity.setThirdName(nullIfBlank(person.path("THIRD_NAME").asText(null)));
        entity.setUnListType(nullIfBlank(person.path("UN_LIST_TYPE").asText(null)));
        entity.setReferenceNumber(nullIfBlank(person.path("REFERENCE_NUMBER").asText(null)));
        entity.setListedOn(parseDate(person.path("LISTED_ON").asText(null), person.path("DATAID").asLong()));
        entity.setNameOriginalScript(nullIfBlank(person.path("NAME_ORIGINAL_SCRIPT").asText(null)));
        entity.setComments1(nullIfBlank(person.path("COMMENTS1").asText(null)));
        entity.setType(ENTITY_TYPE_INDIVIDUAL);
        return entity;
    }

    /**
     * Parses a date string of the form "YYYY-MM-DDThh:mm:ss" or "YYYY-MM-DD".
     * Returns null if the value is blank or unparseable.
     */
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

    /**
     * Returns null for blank/empty strings so we don't persist "" into the DB.
     */
    private String nullIfBlank(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
