package Learn.web.client.controller;

import Learn.web.client.response.BaseResponse;
import Learn.web.client.response.Dto;
import Learn.web.client.service.HtmlFetchService;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xml")
public class HtmlFetchController {

    private final HtmlFetchService htmlFetchService;

    public HtmlFetchController(HtmlFetchService htmlFetchService) {
        this.htmlFetchService = htmlFetchService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> fetchXml() {
        JSONObject json = htmlFetchService.fetchXml();
        return ResponseEntity.ok(json.toString(4));
    }
}
