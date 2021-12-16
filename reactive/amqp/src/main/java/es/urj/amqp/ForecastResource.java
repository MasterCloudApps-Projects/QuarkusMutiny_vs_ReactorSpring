package es.urj.amqp;

import es.urj.amqp.model.City;
import es.urj.amqp.model.Forecast;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.eclipse.microprofile.reactive.messaging.Channel;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/forecast")
public class ForecastResource {

    @Channel("request-forecast")
    MutinyEmitter<Forecast> processForecast;

    @Channel("forecast")
    Multi<Forecast> processedForecasts;

    @POST
    public Uni<Forecast> processForecast(City city) {
        return Uni.createFrom().item(city)
                .map(City::getName)
                .map(cityName -> new Forecast(UUID.randomUUID().toString(), cityName))
                .call(forecast -> processForecast.send(forecast));
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Forecast> getProcessedForecasts() {
        return processedForecasts;
    }
}