package es.urjc.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ListenerService extends DefaultConsumer {

    private static final Logger log = LoggerFactory.getLogger(ListenerService.class);

    public ListenerService(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

        String message = new String(body, StandardCharsets.UTF_8);
        log.info("Received: {}", message);

    }
}
