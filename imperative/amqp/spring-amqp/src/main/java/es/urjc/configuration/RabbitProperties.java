package es.urjc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProperties {

    @Value("${rabbit.exchange.name}")
    private String exchangeName;

    @Value("${rabbit.queue.name}")
    private String queueName;

    @Value("${rabbit.router.key:#}")
    private String routerKey;

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getRouterKey() {
        return routerKey;
    }
}