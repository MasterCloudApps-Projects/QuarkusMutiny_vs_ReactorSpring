package es.urjc.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfiguration {

    private final RabbitProperties rabbitProperties;

    public RabbitConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Queue myQueue() {
        return QueueBuilder.durable(rabbitProperties.getQueueName()).build();
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange(rabbitProperties.getExchangeName());
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(rabbitProperties.getRouterKey())
                .noargs();
    }
}