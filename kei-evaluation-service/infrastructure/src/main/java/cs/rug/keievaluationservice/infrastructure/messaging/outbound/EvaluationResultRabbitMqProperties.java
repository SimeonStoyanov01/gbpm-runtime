package cs.rug.keievaluationservice.infrastructure.messaging.outbound;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gbpmruntime.messaging.evaluation-result")
public class EvaluationResultRabbitMqProperties {
    private String exchangeName;
    private String routingKey;
}
