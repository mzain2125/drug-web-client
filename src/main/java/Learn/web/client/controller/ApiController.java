package Learn.web.client.controller;

import Learn.web.client.response.BaseResponse;
import Learn.web.client.response.Dto;
import Learn.web.client.service.ApiService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping(value = "/drug/{ndc}")
    public ResponseEntity<?> getDrugData(@PathVariable String ndc) {
        BaseResponse<?> response = apiService.getData(ndc);
        return ResponseEntity.ok(response);
    }
}
