package cs.rug.camunda8workerservice.messaging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "camunda8worker.messaging.engine-task-completed")
public class EngineTaskCompletedMessagingProperties {

    private String exchangeName;
    private String routingKey;
}
