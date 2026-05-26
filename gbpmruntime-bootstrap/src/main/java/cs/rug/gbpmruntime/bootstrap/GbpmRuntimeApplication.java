package cs.rug.gbpmruntime.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cs.rug.gbpmruntime")
public class GbpmRuntimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GbpmRuntimeApplication.class, args);
    }
}
