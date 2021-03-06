package es.urjc.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import es.urjc.model.Forecast;
import es.urjc.service.ForecastService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import java.io.IOException;

public abstract class AbstractForecastService implements ForecastService {

    private final Sender sender;
    private final Receiver receiver;
    private final ObjectMapper objectMapper;

    protected AbstractForecastService(Sender sender, Receiver receiver, ObjectMapper objectMapper) {
        this.sender = sender;
        this.receiver = receiver;
        this.objectMapper = objectMapper;
    }

    protected abstract String getExchange();

    protected abstract String getRoutingKey();

    protected abstract String getQueue();

    @Override
    public void send(Forecast forecast) {
        Mono.just(forecast)
                .map(this::generateOutboundMessage)
                .publish(sender::send)
                .subscribe();
    }

    @Override
    public Flux<Forecast> consume() {
        return receiver.consumeAutoAck(this.getQueue())
                .map(Delivery::getBody)
                .map(this::deserializeObject);
    }

    public OutboundMessage generateOutboundMessage(Forecast forecast) {
        return new OutboundMessage(this.getExchange(), this.getRoutingKey(), this.serializeObject(forecast));
    }

    private byte[] serializeObject(Forecast forecast) {
        try {
            return objectMapper.writeValueAsBytes(forecast);
        } catch (JsonProcessingException e) {
            return new byte[]{};
        }
    }

    private Forecast deserializeObject(byte[] bytes) {

        try {
            return objectMapper.readValue(bytes, Forecast.class);
        } catch (IOException e) {
            return new Forecast();
        }

    }


}
