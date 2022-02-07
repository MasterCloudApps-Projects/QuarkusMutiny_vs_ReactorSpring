package es.urjc.configuration;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import es.urjc.service.ListenerService;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.runtime.Startup;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;

@Startup
@ApplicationScoped
public class RabbitConfiguration {

    private final Channel channel;

    @Inject
    public RabbitConfiguration(RabbitMQClient rabbitMQClient, RabbitProperties rabbitProperties) {

        try {

            // Config exchange, queue and binding
            Connection connection = rabbitMQClient.connect();
            channel = connection.createChannel();
            channel.exchangeDeclare(rabbitProperties.getExchangeName(), BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(rabbitProperties.getQueueName(), true, false, false, null);
            channel.queueBind(rabbitProperties.getQueueName(), rabbitProperties.getExchangeName(), rabbitProperties.getRouterKey());

            // Set listener service class
            ListenerService listenerService = new ListenerService(channel);
            channel.basicConsume(rabbitProperties.getQueueName(), true, listenerService);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @Produces
    public Channel getChannel() {
        return this.channel;
    }

}