package es.urjc.controller;

import es.urjc.model.City;
import es.urjc.model.Forecast;
import es.urjc.service.ForecastService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/forecast")
public class ForecastController {

    private final ForecastService processForecastService;
    private final ForecastService processedForecastService;

    public ForecastController(ForecastService processForecastService, ForecastService processedForecastService) {
        this.processForecastService = processForecastService;
        this.processedForecastService = processedForecastService;
    }

    @PostMapping
    public Mono<Forecast> processForecast(@RequestBody City city) {

        Forecast forecast = new Forecast(UUID.randomUUID().toString(), city.getName());

        return Mono.just(forecast)
                .doOnNext(processForecastService::send)
                .thenReturn(forecast);

    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Forecast> receiveMessagesFromQueue() {
        return processedForecastService.consume();
    }

}
