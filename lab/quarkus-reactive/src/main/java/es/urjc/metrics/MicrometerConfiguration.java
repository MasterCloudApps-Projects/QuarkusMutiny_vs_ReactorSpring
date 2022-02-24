package es.urjc.metrics;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import java.util.List;

@ApplicationScoped
public class MicrometerConfiguration {

    @Produces
    @Singleton
    public MeterFilter configureAllRegistries() {
        return MeterFilter.commonTags(List.of(Tag.of("application", "quarkus-reactive")));
    }

}
