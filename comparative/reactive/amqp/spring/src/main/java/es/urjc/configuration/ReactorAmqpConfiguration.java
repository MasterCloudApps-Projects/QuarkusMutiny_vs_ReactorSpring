package es.urjc.configuration;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

@Configuration
public class ReactorAmqpConfiguration {

    @Bean
    public Mono<Connection> reactiveRabbitMQConnection(RabbitProperties rabbitProperties) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return Mono.fromCallable(connectionFactory::newConnection).cache();
    }

    @Bean
    public Exchange processForecastExchange(AmqpProperties amqpProperties) {
        return new DirectExchange(amqpProperties.getProcessForecastExchangeName());
    }

    @Bean
    public Exchange processedForecastExchange(AmqpProperties amqpProperties) {
        return new DirectExchange(amqpProperties.getProcessedForecastExchangeName());
    }

    @Bean
    public Queue processForecastQueue(AmqpProperties amqpProperties) {
        return QueueBuilder.durable(amqpProperties.getProcessForecastQueueName()).build();
    }

    @Bean
    public Queue processedForecastQueue(AmqpProperties amqpProperties) {
        return QueueBuilder.durable(amqpProperties.getProcessedForecastQueueName()).build();
    }

    @Bean
    public Binding processForecastBinding(Queue processForecastQueue, Exchange processForecastExchange,
                                          AmqpProperties rabbitProperties) {

        return BindingBuilder
                .bind(processForecastQueue)
                .to(processForecastExchange)
                .with(rabbitProperties.getRouterKey())
                .noargs();
    }

    @Bean
    public Binding processedForecastBinding(Queue processedForecastQueue, Exchange processedForecastExchange,
                                            AmqpProperties rabbitProperties) {

        return BindingBuilder
                .bind(processedForecastQueue)
                .to(processedForecastExchange)
                .with(rabbitProperties.getRouterKey())
                .noargs();
    }

    @Bean
    public Sender sender(Mono<Connection> reactiveRabbitMQConnection) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(reactiveRabbitMQConnection));
    }

    @Bean
    public Receiver receiver(Mono<Connection> reactiveRabbitMQConnection) {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(reactiveRabbitMQConnection));
    }

    @Bean
    public Flux<Delivery> processForecastListener(final Receiver receiver, Binding processForecastBinding) {
        return receiver.consumeAutoAck(processForecastBinding.getDestination());
    }

    @Bean
    public Flux<Delivery> processedForecastListener(final Receiver receiver, Binding processedForecastBinding) {
        return receiver.consumeAutoAck(processedForecastBinding.getDestination());
    }


}
