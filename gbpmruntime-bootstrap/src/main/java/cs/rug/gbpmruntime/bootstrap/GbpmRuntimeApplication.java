package cs.rug.gbpmruntime.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "cs.rug.gbpmruntime")
@EnableFeignClients(basePackages = "cs.rug.gbpmruntime.processregistry.infrastructure")

public class GbpmRuntimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GbpmRuntimeApplication.class, args);
    }
}
