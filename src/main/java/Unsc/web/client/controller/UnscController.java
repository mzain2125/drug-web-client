package Unsc.web.client.controller;


import Unsc.web.client.service.UnscService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xml")
public class UnscController {

    private final UnscService unscService;

    public UnscController(UnscService unscService) {
        this.unscService = unscService;
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> fetchXml(){
        String  json = unscService.fetchAndSave();
        return ResponseEntity.ok(json);
    }
}
