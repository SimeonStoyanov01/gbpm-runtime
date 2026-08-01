package cs.rug.co2calculationservice.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "cs.rug.co2calculationservice")
@EntityScan("cs.rug.co2calculationservice.infrastructure.persistence.entity")
@EnableJpaRepositories(basePackages = "cs.rug.co2calculationservice.infrastructure.persistence.repository")
public class Co2CalculationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Co2CalculationServiceApplication.class, args);
    }
}
