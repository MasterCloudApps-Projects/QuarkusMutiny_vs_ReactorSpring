package es.urjc.spring.rest.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping("/cat-fact")
@RestController
public class CatFactController {

    private final ReactiveWebClient webClient;

    public CatFactController(ReactiveWebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping
    public Mono<CatFact> getFact() {
        return webClient.getFact();
    }
}
