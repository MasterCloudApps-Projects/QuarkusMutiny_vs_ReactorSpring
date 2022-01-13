package es.urjc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ListenerService {

    private static final Logger log = LoggerFactory.getLogger(ListenerService.class);

    @RabbitListener(queues = "#{rabbitProperties.getQueueName()}")
    public void handleDelivery(String message) {
        log.info("Received: {}", message);
    }
}
