package cs.rug.processregistryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "cs.rug.processregistryservice")
@EnableFeignClients(basePackages = "cs.rug.processregistryservice.infrastructure.client")
public class ProcessRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessRegistryApplication.class, args);
    }
}
