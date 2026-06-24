package cs.rug.gbpmruntime.observation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "cs.rug.gbpmruntime.observation")
@EnableFeignClients(basePackages = "cs.rug.gbpmruntime.observation.infrastructure.client")
public class ObservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObservationApplication.class, args);
    }
}
