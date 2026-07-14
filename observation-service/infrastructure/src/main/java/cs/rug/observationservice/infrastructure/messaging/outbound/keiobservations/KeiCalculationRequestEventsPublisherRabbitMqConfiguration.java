package cs.rug.observationservice.infrastructure.messaging.outbound.keiobservations;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeiCalculationRequestEventsRabbitMqProperties.class)
public class KeiCalculationRequestEventsPublisherRabbitMqConfiguration {

    @Bean
    public TopicExchange keiCalculationRequestEventsExchange(KeiCalculationRequestEventsRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }
}
