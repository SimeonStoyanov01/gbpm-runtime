package cs.rug.camunda8workerservice.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EngineTaskCompletedMessagingProperties.class)
public class RabbitMqConfiguration {

    @Bean
    public TopicExchange engineTaskCompletedExchange(
            EngineTaskCompletedMessagingProperties properties
    ) {
        return new TopicExchange(properties.getExchangeName(), true, false);
    }
}
