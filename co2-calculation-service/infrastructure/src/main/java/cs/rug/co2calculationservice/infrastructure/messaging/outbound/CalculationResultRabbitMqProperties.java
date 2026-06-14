package cs.rug.co2calculationservice.infrastructure.messaging.outbound;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gbpmruntime.messaging.calculation-result")
public class CalculationResultRabbitMqProperties {
    private String exchangeName;
    private String routingKey;
}
