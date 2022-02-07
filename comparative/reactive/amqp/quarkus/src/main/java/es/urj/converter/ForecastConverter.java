package es.urj.converter;

import io.smallrye.reactive.messaging.MessageConverter;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.Type;

@ApplicationScoped
public class ForecastConverter implements MessageConverter {

    @Override
    public boolean canConvert(Message<?> in, Type target) {
        return true;
    }

    @Override
    public Message<?> convert(Message<?> in, Type target) {
        return in
                .withPayload(((JsonObject) in.getPayload())
                .mapTo((Class<?>) target));
    }
}