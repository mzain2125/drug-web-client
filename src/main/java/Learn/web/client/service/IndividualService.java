package Learn.web.client.service;

import Learn.web.client.entity.*;
import Learn.web.client.repository.IndividualRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Service
public class IndividualService {

    private final WebClient webClient;
    private final IndividualRepository individualRepository;

    public IndividualService(WebClient webClient, IndividualRepository individualRepository) {
        this.webClient = webClient;
        this.individualRepository = individualRepository;
    }

    public JSONObject fetchXmlAndSave() {
        // Fetch XML from URL
        String xml = webClient.get()
                .uri("https://unsolprodfiles.blob.core.windows.net/publiclegacyxmlfiles/EN/consolidatedLegacyByPRN.xml")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (xml == null) {
            throw new RuntimeException("The given XML returned no value");
        }

        // Convert XML to JSON
        JSONObject jsonObject = XML.toJSONObject(xml);

//        JSONArray individualsArray = jsonObject
//                .getJSONObject("CONSOLIDATED_LIST")
//                .getJSONObject("INDIVIDUALS")
//                .getJSONArray("INDIVIDUAL");
//
//        for (int i = 0; i < individualsArray.length(); i++) {
//            JSONObject ind = individualsArray.getJSONObject(i);
//
//            // --- Create Individual ---
//            Individual individual = new Individual();
//            individual.setDataId(ind.optLong("DATAID"));
//            individual.setFirstName(ind.optString("FIRST_NAME"));
//            individual.setSecondName(ind.optString("SECOND_NAME"));
//            individual.setReferenceNumber(ind.optString("REFERENCE_NUMBER"));
//            individual.setUnListType(ind.optString("UN_LIST_TYPE"));
//            individual.setGender(ind.optString("GENDER"));
////            individual.setComments(ind.optString("COMMENTS1"));
//
//            // Listed On
//            String listedOnStr = ind.optString("LISTED_ON");
//            if (!listedOnStr.isEmpty()) {
//                try {
//                    individual.setListedOn(LocalDate.parse(listedOnStr));
//                } catch (Exception e) {
//                    individual.setListedOn(null);
//                }
//            }
//
//            // --- Nationalities ---
//            individual.setNationalities(parseChildList(ind.opt("NATIONALITY"), natJson -> {
//                Nationality n = new Nationality();
//                n.setValue(natJson.optString("VALUE"));
//                n.setIndividual(individual);
//                return n;
//            }));
//
//            // --- Aliases ---
//            individual.setAliases(parseChildList(ind.opt("INDIVIDUAL_ALIAS"), aliasJson -> {
//                String aliasName = aliasJson.optString("ALIAS_NAME");
//                if (aliasName.isEmpty()) return null;
//                IndividualAlias alias = new IndividualAlias();
//                alias.setAliasName(aliasName);
//                alias.setIndividual(individual);
//                return alias;
//            }));
//
//            // --- Addresses ---
//            individual.setAddresses(parseChildList(ind.opt("INDIVIDUAL_ADDRESS"), addrJson -> {
//                String country = addrJson.optString("COUNTRY");
//                String note = addrJson.optString("NOTE");
//                if (country.isEmpty() && note.isEmpty()) return null;
//                IndividualAddress addr = new IndividualAddress();
//                addr.setCountry(country);
//                addr.setNote(note);
//                addr.setIndividual(individual);
//                return addr;
//            }));
//
//            // --- Date of Birth ---
//            individual.setDateOfBirths(parseChildList(ind.opt("INDIVIDUAL_DATE_OF_BIRTH"), dobJson -> {
//                int year = dobJson.optInt("YEAR", 0);
//                if (year == 0) return null;
//                IndividualDateOfBirth dob = new IndividualDateOfBirth();
//                dob.setTypeOfDate(dobJson.optString("TYPE_OF_DATE"));
//                dob.setIndividual(individual);
//                return dob;
//            }));
//
//            // --- Last Day Updated ---
//            individual.setLastDayUpdatedList(parseChildList(ind.opt("LAST_DAY_UPDATED"), luJson -> {
//                String value = luJson.optString("VALUE");
//                if (value.isEmpty()) return null;
//                LastDayUpdated lu = new LastDayUpdated();
//                try {
//                    lu.setUpdatedDate(LocalDate.parse(value));
//                } catch (Exception e) {
//                    lu.setUpdatedDate(null);
//                }
//                lu.setIndividual(individual);
//                return lu;
//            }));
//
//            // --- Save Individual with cascading children ---
//            individualRepository.save(individual);
//        }
//
//        return jsonObject;
//    }
//
//    // --- Helper method to handle JSONObject or JSONArray ---
//    private <T> List<T> parseChildList(Object childObj, Function<JSONObject, T> mapper) {
//        List<T> list = new ArrayList<>();
//        if (childObj == null) return list;
//
//        if (childObj instanceof JSONObject) {
//            T mapped = mapper.apply((JSONObject) childObj);
//            if (mapped != null) list.add(mapped);
//        } else if (childObj instanceof JSONArray) {
//            JSONArray array = (JSONArray) childObj;
//            for (int i = 0; i < array.length(); i++) {
//                T mapped = mapper.apply(array.getJSONObject(i));
//                if (mapped != null) list.add(mapped);
//            }
//        }
        return jsonObject;
    }
}
