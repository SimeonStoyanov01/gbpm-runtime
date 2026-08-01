package cs.rug.camunda8workerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Camunda8WorkerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Camunda8WorkerServiceApplication.class, args);
    }
}
