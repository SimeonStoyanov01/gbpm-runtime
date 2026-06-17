package cs.rug.monitoringresultsservice.infrastructure.messaging.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        CalculationResultRabbitMqProperties.class,
        EvaluationResultRabbitMqProperties.class,
        ThresholdViolationRabbitMqProperties.class
})
public class MonitoringRabbitMqConfiguration {

    @Bean
    public Queue monitoringCalculationResultQueue(CalculationResultRabbitMqProperties properties) {
        return new Queue(properties.getQueueName(), true);
    }

    @Bean
    public TopicExchange monitoringCalculationResultExchange(CalculationResultRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }

    @Bean
    public Binding monitoringCalculationResultBinding(
            Queue monitoringCalculationResultQueue,
            TopicExchange monitoringCalculationResultExchange,
            CalculationResultRabbitMqProperties properties
    ) {
        return BindingBuilder
                .bind(monitoringCalculationResultQueue)
                .to(monitoringCalculationResultExchange)
                .with(properties.getRoutingKey());
    }

    @Bean
    public Queue monitoringEvaluationResultQueue(EvaluationResultRabbitMqProperties properties) {
        return new Queue(properties.getQueueName(), true);
    }

    @Bean
    public TopicExchange monitoringEvaluationResultExchange(EvaluationResultRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }

    @Bean
    public Binding monitoringEvaluationResultBinding(
            Queue monitoringEvaluationResultQueue,
            TopicExchange monitoringEvaluationResultExchange,
            EvaluationResultRabbitMqProperties properties
    ) {
        return BindingBuilder
                .bind(monitoringEvaluationResultQueue)
                .to(monitoringEvaluationResultExchange)
                .with(properties.getRoutingKey());
    }

    @Bean
    public Queue monitoringThresholdViolationQueue(ThresholdViolationRabbitMqProperties properties) {
        return new Queue(properties.getQueueName(), true);
    }

    @Bean
    public TopicExchange monitoringThresholdViolationExchange(ThresholdViolationRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }

    @Bean
    public Binding monitoringThresholdViolationBinding(
            Queue monitoringThresholdViolationQueue,
            TopicExchange monitoringThresholdViolationExchange,
            ThresholdViolationRabbitMqProperties properties
    ) {
        return BindingBuilder
                .bind(monitoringThresholdViolationQueue)
                .to(monitoringThresholdViolationExchange)
                .with(properties.getRoutingKey());
    }
}
