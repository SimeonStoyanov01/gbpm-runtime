package cs.rug.co2calculationservice.infrastructure.messaging.inbound;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CalculationRequestRabbitMqProperties.class)
public class CalculationRequestRabbitMqConfiguration {

    @Bean
    public Queue calculationRequestQueue(CalculationRequestRabbitMqProperties properties) {
        return new Queue(properties.getQueueName(), true);
    }

    @Bean
    public TopicExchange calculationRequestExchange(CalculationRequestRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }

    @Bean
    public Binding calculationRequestBinding(
            Queue calculationRequestQueue,
            TopicExchange calculationRequestExchange,
            CalculationRequestRabbitMqProperties properties
    ) {
        return BindingBuilder
                .bind(calculationRequestQueue)
                .to(calculationRequestExchange)
                .with(properties.getRoutingKey());
    }
}
