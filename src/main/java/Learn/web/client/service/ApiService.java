package Learn.web.client.service;

import Learn.web.client.response.BaseResponse;
import Learn.web.client.response.Dto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiService {

    private final WebClient webClient;

    public ApiService(WebClient webClient){
        this.webClient = webClient;
    }

    public BaseResponse<Dto> getData(String ndc) {

        return webClient.get()
                .uri("http://localhost:8989/api/drug/ndc/{ndc}", ndc)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponse<Dto>>() {})
                .block();
    }


}
