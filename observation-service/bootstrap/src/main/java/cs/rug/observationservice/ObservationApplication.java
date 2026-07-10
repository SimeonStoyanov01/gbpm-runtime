package cs.rug.observationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "cs.rug.observationservice")
@EnableFeignClients(basePackages = "cs.rug.observationservice.infrastructure.client")
public class ObservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObservationApplication.class, args);
    }
}
