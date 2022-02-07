package es.urj.amqp;


import es.urj.amqp.model.Forecast;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@ApplicationScoped
public class ForecastProcessor {

    private final Random random = new Random();
    private final String regularExpression = "^[a-mA-M].*";
    private final List<String> weatherList = Arrays.asList("sunny", "raining", "windy");

    @Incoming("process-forecast")
    @Outgoing("processed-forecast")
    public Uni<Forecast> listener(Forecast receivedForecast) {
        return Uni.createFrom().item(receivedForecast)
                .map(forecast -> {
                    String weather = getRandomWeather(forecast.getCity());
                    return new Forecast(forecast.getId(), forecast.getCity(), weather);
                })
                .onItem().delayIt().by(getRandomDuration());
    }

    private Duration getRandomDuration() {
        int value = random.nextInt(5) + 1;
        return Duration.ofSeconds(value);
    }

    private String getRandomWeather(String value) {
        String weather = weatherList.get(random.nextInt(weatherList.size()));
        return Pattern.matches(regularExpression, value)
                ? weather.toUpperCase()
                : weather.toLowerCase();
    }
}