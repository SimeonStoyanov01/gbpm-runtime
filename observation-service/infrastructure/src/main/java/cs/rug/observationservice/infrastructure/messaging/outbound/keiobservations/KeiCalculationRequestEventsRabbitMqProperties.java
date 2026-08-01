package cs.rug.observationservice.infrastructure.messaging.outbound.keiobservations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "runtime.observation.messaging.calculation-request-events")
public class KeiCalculationRequestEventsRabbitMqProperties {

    private String exchangeName;

    private String calculationRequestedRoutingKeyPrefix;

}
