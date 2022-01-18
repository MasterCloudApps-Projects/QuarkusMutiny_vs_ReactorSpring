package es.urjc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpProperties {

    @Value("${rabbit.process-forecast.exchange.name}")
    private String processForecastExchangeName;

    @Value("${rabbit.processed-forecast.exchange.name}")
    private String processedForecastExchangeName;

    @Value("${rabbit.process-forecast.queue.name}")
    private String processForecastQueueName;

    @Value("${rabbit.processed-forecast.queue.name}")
    private String processedForecastQueueName;

    @Value("${rabbit.router.key:#}")
    private String routerKey;

    public String getProcessForecastExchangeName() {
        return processForecastExchangeName;
    }

    public String getProcessedForecastExchangeName() {
        return processedForecastExchangeName;
    }

    public String getProcessForecastQueueName() {
        return processForecastQueueName;
    }

    public String getProcessedForecastQueueName() {
        return processedForecastQueueName;
    }

    public String getRouterKey() {
        return routerKey;
    }
}