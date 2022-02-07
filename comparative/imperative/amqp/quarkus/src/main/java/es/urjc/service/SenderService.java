package es.urjc.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import es.urjc.configuration.RabbitConfiguration;
import es.urjc.configuration.RabbitProperties;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class SenderService {

    private final Channel channel;
    private final RabbitProperties rabbitProperties;

    public SenderService(RabbitConfiguration rabbitConfiguration, RabbitProperties rabbitProperties) {
        this.channel = rabbitConfiguration.getChannel();
        this.rabbitProperties = rabbitProperties;
    }

    public void send(String message) {

        try {

            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();

            channel.basicPublish(
                    rabbitProperties.getExchangeName(), rabbitProperties.getRouterKey(),
                    basicProperties, messageBytes);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
