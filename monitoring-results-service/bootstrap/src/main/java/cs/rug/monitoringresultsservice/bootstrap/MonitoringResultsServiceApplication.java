package cs.rug.monitoringresultsservice.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cs.rug.monitoringresultsservice")
public class MonitoringResultsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringResultsServiceApplication.class, args);
    }
}
