package Learn.web.client.controller;


import Learn.web.client.service.UnscService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
