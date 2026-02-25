package Learn.web.client.service;

import Learn.web.client.entity.Individual;
import Learn.web.client.repository.IndividualRepository;
import Learn.web.client.response.BaseResponse;
import Learn.web.client.response.Dto;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.boot.spi.XmlMappingBinderAccess;
import org.hibernate.type.format.jackson.JacksonXmlFormatMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HtmlFetchService {

    private final WebClient webClient;

    private final IndividualRepository individualRepository;

    public HtmlFetchService(WebClient webClient, IndividualRepository individualRepository) {
        this.webClient = webClient;
        this.individualRepository = individualRepository;
    }

    public JSONObject fetchXml() {
        String xml=webClient
                .get()
                .uri("https://unsolprodfiles.blob.core.windows.net/publiclegacyxmlfiles/EN/consolidatedLegacyByPRN.xml")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assert xml != null;
        JSONObject jsonObject= XML.toJSONObject(xml);


        JSONArray individualsArray = jsonObject
                .getJSONObject("CONSOLIDATED_LIST")
                .getJSONObject("INDIVIDUALS")
                .getJSONArray("INDIVIDUAL");

        for (int i = 0; i < individualsArray.length(); i++) {
            JSONObject ind = individualsArray.getJSONObject(i);

            Individual individual = new Individual();
            individual.setDataId(ind.optLong("DATAID"));
            individual.setFirstName(ind.optString("FIRST_NAME"));
            individual.setSecondName(ind.optString("SECOND_NAME"));
            individual.setReferenceNumber(ind.optString("REFERENCE_NUMBER"));

            // LISTED_ON parsing with fallback
            String listedOnStr = ind.optString("LISTED_ON");
            if (!listedOnStr.isEmpty()) {
                try {
                    individual.setListedOn(LocalDate.parse(listedOnStr));
                } catch (Exception e) {
                    // fallback or skip if invalid date
                    individual.setListedOn(null);
                }
            }

            // NATIONALITY from nested object
            String nationality = "";
            if (ind.has("NATIONALITY")) {
                JSONObject nat = ind.optJSONObject("NATIONALITY");
                if (nat != null) {
                    nationality = nat.optString("VALUE");
                }
            }
            individual.setNationality(nationality);

            // Save to DB
            individualRepository.save(individual);
        }

        return jsonObject;
    }

}

