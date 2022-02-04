package es.urjc.spring.rest.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ReactiveWebClient {

    public static final String CAT_BASE_URL = "https://catfact.ninja";
    private final WebClient webClient;

    public ReactiveWebClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(CAT_BASE_URL).build();
    }

    public Mono<CatFact> getFact() {
        return this.webClient.get()
                .uri("/fact")
                .retrieve()
                .bodyToMono(CatFact.class);
    }

}



