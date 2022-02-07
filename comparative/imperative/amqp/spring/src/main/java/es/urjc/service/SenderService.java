package es.urjc.service;

import es.urjc.configuration.RabbitProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    public SenderService(RabbitTemplate rabbitTemplate, RabbitProperties rabbitProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitProperties = rabbitProperties;
    }

    public void send(String message) {
        rabbitTemplate.convertAndSend(rabbitProperties.getQueueName(), message);
    }

}
