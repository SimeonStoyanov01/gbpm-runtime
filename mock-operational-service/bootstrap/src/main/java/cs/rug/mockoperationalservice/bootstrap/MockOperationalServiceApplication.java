package cs.rug.mockoperationalservice.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cs.rug.mockoperationalservice")
public class MockOperationalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockOperationalServiceApplication.class, args);
    }
}
