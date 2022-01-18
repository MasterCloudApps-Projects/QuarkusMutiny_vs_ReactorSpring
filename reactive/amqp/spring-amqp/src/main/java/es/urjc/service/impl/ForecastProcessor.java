package es.urjc.service.impl;


import es.urjc.model.Forecast;
import es.urjc.service.ForecastService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Component
public class ForecastProcessor {

    private final Random random = new Random();
    private final String regularExpression = "^[a-mA-M].*";
    private final List<String> weatherList = Arrays.asList("sunny", "raining", "windy");

    private final ForecastService processForecastService;
    private final ForecastService processedForecastService;
    ;

    public ForecastProcessor(ForecastService processForecastService, ForecastService processedForecastService) {
        this.processForecastService = processForecastService;
        this.processedForecastService = processedForecastService;
    }

    @PostConstruct
    public void listener() {

        this.processForecastService.consume()
                .map(forecast -> {
                    String weather = getRandomWeather(forecast.getCity());
                    return new Forecast(forecast.getId(), forecast.getCity(), weather);
                })
                .delayElements(getRandomDuration())
                .doOnNext(processedForecastService::send)
                .subscribe();

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
