package cs.rug.camunda8integration.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cs.rug.camunda8integration")
public class Camunda8IntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(Camunda8IntegrationApplication.class, args);
    }
}
