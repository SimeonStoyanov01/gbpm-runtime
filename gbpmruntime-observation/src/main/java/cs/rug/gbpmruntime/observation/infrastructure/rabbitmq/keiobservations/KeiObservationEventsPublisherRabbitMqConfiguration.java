package cs.rug.gbpmruntime.observation.infrastructure.rabbitmq.keiobservations;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeiObservationEventsRabbitMqProperties.class)
public class KeiObservationEventsPublisherRabbitMqConfiguration {

    @Bean
    public TopicExchange keiObservationEventsExchange(KeiObservationEventsRabbitMqProperties properties) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }
}
