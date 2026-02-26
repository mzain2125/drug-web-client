package Learn.web.client.controller;


import Learn.web.client.service.IndividualService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xml")
public class IndividualController {

    private final IndividualService individualService;

    public IndividualController(IndividualService individualService) {
        this.individualService = individualService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> fetchXml() {
        JSONObject json = individualService.fetchXmlAndSave();
        return ResponseEntity.ok(json.toString(4));
    }
}
