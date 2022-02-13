package es.urjc.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.stereotype.Service;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

@Service
public class ProcessForecastService extends AbstractForecastService {

    private final Binding processForecastBinding;

    public ProcessForecastService(Sender sender, Receiver receiver, ObjectMapper objectMapper, Binding processForecastBinding) {
        super(sender, receiver, objectMapper);
        this.processForecastBinding = processForecastBinding;
    }

    @Override
    protected String getExchange() {
        return processForecastBinding.getExchange();
    }

    @Override
    protected String getRoutingKey() {
        return processForecastBinding.getRoutingKey();
    }

    @Override
    protected String getQueue() {
        return processForecastBinding.getDestination();
    }
}
