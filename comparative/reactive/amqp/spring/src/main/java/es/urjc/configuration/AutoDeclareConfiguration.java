package es.urjc.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AutoDeclareConfiguration {

    public AutoDeclareConfiguration(AmqpAdmin amqpAdmin, List<Binding> bindings) {
        bindings.forEach(amqpAdmin::declareBinding);
    }
}
