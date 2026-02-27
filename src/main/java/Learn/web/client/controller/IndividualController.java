package Learn.web.client.controller;


import Learn.web.client.service.UnscService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xml")
@RequiredArgsConstructor
public class IndividualController {

    private final UnscService unscService;

    @GetMapping("/fetch")
    public ResponseEntity<?> fetchXml() throws JsonProcessingException {
        String  json = unscService.fetchAndSave();
        return ResponseEntity.ok(json);
    }
}
