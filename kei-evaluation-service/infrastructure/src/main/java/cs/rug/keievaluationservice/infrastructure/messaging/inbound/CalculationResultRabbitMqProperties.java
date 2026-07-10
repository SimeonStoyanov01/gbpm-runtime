package cs.rug.keievaluationservice.infrastructure.messaging.inbound;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "runtime.messaging.calculation-result")
public class CalculationResultRabbitMqProperties {
    private String exchangeName;
    private String routingKey;
    private String queueName;
}
