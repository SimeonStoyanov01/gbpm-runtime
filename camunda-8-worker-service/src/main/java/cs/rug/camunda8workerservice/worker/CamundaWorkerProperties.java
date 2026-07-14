package cs.rug.camunda8workerservice.worker;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "camunda8worker.worker")
public class CamundaWorkerProperties {

    private String engineType;
    private String successfulStatus;
    private String orderIdVariable;
    private String workObjectVariable;
}
