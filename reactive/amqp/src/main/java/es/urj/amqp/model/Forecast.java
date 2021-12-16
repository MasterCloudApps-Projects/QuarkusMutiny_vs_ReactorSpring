package es.urj.amqp.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

public class Forecast {

    public String id;
    public String city;
    public String weather;

    public Forecast() { }

    public Forecast(String id, String city) {
        this.id = id;
        this.city = city;
    }

    public Forecast(String id, String city, String weather) {
        this.id = id;
        this.city = city;
        this.weather = weather;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getWeather() {
        return weather;
    }

}
