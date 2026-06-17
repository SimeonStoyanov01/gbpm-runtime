package cs.rug.monitoringresultsservice.infrastructure.messaging.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gbpmruntime.messaging.threshold-violation")
public class ThresholdViolationRabbitMqProperties {
    private String exchangeName;
    private String routingKey;
    private String queueName;
}
