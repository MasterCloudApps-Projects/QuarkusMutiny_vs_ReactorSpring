package es.urjc.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.stereotype.Service;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

@Service
public class ProcessedForecastService extends AbstractForecastService {

    private final Binding processedForecastBinding;

    public ProcessedForecastService(Sender sender, Receiver receiver, ObjectMapper objectMapper, Binding processedForecastBinding) {
        super(sender, receiver, objectMapper);
        this.processedForecastBinding = processedForecastBinding;
    }

    @Override
    protected String getExchange() {
        return processedForecastBinding.getExchange();
    }

    @Override
    protected String getRoutingKey() {
        return processedForecastBinding.getRoutingKey();
    }

    @Override
    protected String getQueue() {
        return processedForecastBinding.getDestination();
    }
}
