package cs.rug.gbpmruntime.observation.infrastructure.messaging.outbound.keiobservations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "gbpmruntime.observation.messaging.kei-observation-events")
public class KeiObservationEventsRabbitMqProperties {

    private String exchangeName;

    private String observationCreatedRoutingKey;

    private String calculationRequestedRoutingKey;

}
