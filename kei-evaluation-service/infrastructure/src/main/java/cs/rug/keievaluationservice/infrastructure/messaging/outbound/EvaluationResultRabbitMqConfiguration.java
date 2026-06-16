package cs.rug.keievaluationservice.infrastructure.messaging.outbound;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        EvaluationResultRabbitMqProperties.class,
        ThresholdViolationRabbitMqProperties.class
})
public class EvaluationResultRabbitMqConfiguration {

    @Bean
    public TopicExchange evaluationResultExchange(EvaluationResultRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }
}
