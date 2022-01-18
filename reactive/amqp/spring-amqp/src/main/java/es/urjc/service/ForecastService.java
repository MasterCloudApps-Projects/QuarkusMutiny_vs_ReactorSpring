package es.urjc.service;

import es.urjc.model.Forecast;
import reactor.core.publisher.Flux;

public interface ForecastService {

    void send(Forecast forecast);

    Flux<Forecast> consume();

}
