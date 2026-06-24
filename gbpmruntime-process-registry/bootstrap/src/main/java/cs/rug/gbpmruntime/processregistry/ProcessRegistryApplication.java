package cs.rug.gbpmruntime.processregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "cs.rug.gbpmruntime.processregistry")
@EnableFeignClients(basePackages = "cs.rug.gbpmruntime.processregistry.infrastructure.client")
public class ProcessRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessRegistryApplication.class, args);
    }
}
