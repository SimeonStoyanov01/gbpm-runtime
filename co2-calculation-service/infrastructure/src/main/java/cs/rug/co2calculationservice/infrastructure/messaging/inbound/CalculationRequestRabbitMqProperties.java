package cs.rug.co2calculationservice.infrastructure.messaging.inbound;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gbpmruntime.messaging.calculation-request")
public class CalculationRequestRabbitMqProperties {
    private String exchangeName;
    private String routingKey;
    private String queueName;
}
