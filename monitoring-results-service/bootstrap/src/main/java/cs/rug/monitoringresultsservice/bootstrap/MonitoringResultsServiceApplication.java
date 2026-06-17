package cs.rug.monitoringresultsservice.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "cs.rug.monitoringresultsservice")
@EntityScan("cs.rug.monitoringresultsservice.infrastructure.persistence.entity")
@EnableJpaRepositories("cs.rug.monitoringresultsservice.infrastructure.persistence.repository")
public class MonitoringResultsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringResultsServiceApplication.class, args);
    }
}
