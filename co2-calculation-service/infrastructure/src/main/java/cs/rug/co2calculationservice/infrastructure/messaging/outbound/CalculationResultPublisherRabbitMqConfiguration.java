package cs.rug.co2calculationservice.infrastructure.messaging.outbound;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CalculationResultRabbitMqProperties.class)
public class CalculationResultPublisherRabbitMqConfiguration {

    @Bean
    public TopicExchange calculationResultExchange(CalculationResultRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }
}
