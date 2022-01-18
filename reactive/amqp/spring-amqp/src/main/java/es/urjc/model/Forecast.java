package es.urjc.model;

import lombok.ToString;

@ToString
public class Forecast {

    public String id;
    public String city;
    public String weather;

    public Forecast() {
    }

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
