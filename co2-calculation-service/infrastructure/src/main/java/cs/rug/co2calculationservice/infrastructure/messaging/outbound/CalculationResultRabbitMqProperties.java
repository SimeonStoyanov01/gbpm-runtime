package cs.rug.co2calculationservice.infrastructure.messaging.outbound;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "runtime.messaging.calculation-result")
public class CalculationResultRabbitMqProperties {
    @NotBlank
    private String exchangeName;

    @NotBlank
    private String completedRoutingKey;

    @NotBlank
    private String failedRoutingKey;
}
