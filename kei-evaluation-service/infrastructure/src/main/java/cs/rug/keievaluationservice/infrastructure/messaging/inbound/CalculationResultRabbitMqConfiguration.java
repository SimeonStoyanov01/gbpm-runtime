package cs.rug.keievaluationservice.infrastructure.messaging.inbound;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CalculationResultRabbitMqProperties.class)
public class CalculationResultRabbitMqConfiguration {

    @Bean
    public Queue calculationResultQueue(CalculationResultRabbitMqProperties properties) {
        return new Queue(properties.getQueueName(), true);
    }

    @Bean
    public TopicExchange calculationResultInputExchange(CalculationResultRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }

    @Bean
    public Binding calculationResultBinding(
            Queue calculationResultQueue,
            TopicExchange calculationResultInputExchange,
            CalculationResultRabbitMqProperties properties
    ) {
        return BindingBuilder
                .bind(calculationResultQueue)
                .to(calculationResultInputExchange)
                .with(properties.getRoutingKey());
    }
}
