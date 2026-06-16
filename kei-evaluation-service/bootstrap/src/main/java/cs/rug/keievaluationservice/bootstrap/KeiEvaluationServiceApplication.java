package cs.rug.keievaluationservice.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cs.rug.keievaluationservice")
public class KeiEvaluationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeiEvaluationServiceApplication.class, args);
    }
}
