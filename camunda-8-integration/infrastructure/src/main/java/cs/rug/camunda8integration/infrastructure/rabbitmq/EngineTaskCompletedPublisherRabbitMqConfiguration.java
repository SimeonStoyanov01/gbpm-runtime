package cs.rug.camunda8integration.infrastructure.rabbitmq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EngineTaskCompletedPublisherRabbitMqConfiguration {

    @Bean
    public TopicExchange engineTaskCompletedExchange(
            @Value("${camunda8integration.messaging.engine-task-completed.exchange-name}") String exchangeName
    ) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
