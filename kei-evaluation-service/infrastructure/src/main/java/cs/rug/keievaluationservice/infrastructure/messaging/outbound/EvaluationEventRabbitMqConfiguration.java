package cs.rug.keievaluationservice.infrastructure.messaging.outbound;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EvaluationEventRabbitMqProperties.class)
public class EvaluationEventRabbitMqConfiguration {

    @Bean
    public TopicExchange evaluationEventExchange(EvaluationEventRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }
}
