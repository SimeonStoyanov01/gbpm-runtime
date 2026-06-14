package cs.rug.co2calculationservice.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cs.rug.co2calculationservice")
public class Co2CalculationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Co2CalculationServiceApplication.class, args);
    }
}
