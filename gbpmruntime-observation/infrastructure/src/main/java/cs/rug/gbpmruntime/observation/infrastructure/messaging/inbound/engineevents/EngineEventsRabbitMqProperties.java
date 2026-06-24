package cs.rug.gbpmruntime.observation.infrastructure.messaging.inbound.engineevents;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "gbpmruntime.observation.messaging.engine-events")
public class EngineEventsRabbitMqProperties {

    private String queueName;

    private List<Binding> bindings = new ArrayList<>();

    @Setter
    @Getter
    public static class Binding {

        private String exchangeName;

        private String routingKey;

    }
}
