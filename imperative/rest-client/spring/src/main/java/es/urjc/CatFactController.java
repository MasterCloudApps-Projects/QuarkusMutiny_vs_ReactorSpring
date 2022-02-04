package es.urjc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/cat-fact")
@RestController
public class CatFactController {

    @Value("${cat-fact.rest.url}")
    String catFactUrl;

    private final RestTemplate restTemplate;

    public CatFactController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public CatFact getFact() {
        return restTemplate.getForObject(
                catFactUrl, CatFact.class);
    }
}
