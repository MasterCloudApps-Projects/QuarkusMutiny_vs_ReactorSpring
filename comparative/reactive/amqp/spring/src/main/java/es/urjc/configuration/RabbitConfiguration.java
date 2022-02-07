package es.urjc.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

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
                                          AmqpAdmin amqpAdmin, AmqpProperties rabbitProperties) {
        Binding processForecastBinding = BindingBuilder
                .bind(processForecastQueue)
                .to(processForecastExchange)
                .with(rabbitProperties.getRouterKey())
                .noargs();

        amqpAdmin.declareBinding(processForecastBinding);

        return processForecastBinding;
    }

    @Bean
    public Binding processedForecastBinding(Queue processedForecastQueue, Exchange processedForecastExchange,
                                            AmqpAdmin amqpAdmin, AmqpProperties rabbitProperties) {

        Binding processedForecastBinding = BindingBuilder
                .bind(processedForecastQueue)
                .to(processedForecastExchange)
                .with(rabbitProperties.getRouterKey())
                .noargs();

        amqpAdmin.declareBinding(processedForecastBinding);

        return processedForecastBinding;
    }

}
