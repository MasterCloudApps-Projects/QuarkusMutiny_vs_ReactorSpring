package es.urjc.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RabbitProperties {

    @ConfigProperty(name = "rabbit.exchange.name")
    String exchangeName;

    @ConfigProperty(name = "rabbit.queue.name")
    String queueName;

    @ConfigProperty(name = "rabbit.router.key", defaultValue = "#")
    String routerKey;

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