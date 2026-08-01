package cs.rug.keievaluationservice.infrastructure.messaging.outbound;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "runtime.messaging.evaluation.events")
public class EvaluationEventRabbitMqProperties {
    @NotBlank
    private String exchangeName;

    @NotBlank
    private String evaluationRoutingKey;
}
