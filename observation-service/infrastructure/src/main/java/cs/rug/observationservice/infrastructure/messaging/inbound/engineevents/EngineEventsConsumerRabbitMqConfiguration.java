package cs.rug.observationservice.infrastructure.messaging.inbound.engineevents;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EngineEventsRabbitMqProperties.class)
public class EngineEventsConsumerRabbitMqConfiguration {

    @Bean
    public Queue engineEventsQueue(EngineEventsRabbitMqProperties properties) {
        return new Queue(properties.getQueueName(), true);
    }

    @Bean
    public Declarables engineEventsBindings(
            Queue engineEventsQueue,
            EngineEventsRabbitMqProperties properties
    ) {
        List<Declarable> declarables = properties.getBindings().stream()
                .flatMap(binding -> {
                    TopicExchange exchange = new TopicExchange(binding.getExchangeName(), true, false);
                    return Stream.<Declarable>of(
                            exchange,
                            BindingBuilder.bind(engineEventsQueue).to(exchange).with(binding.getRoutingKey())
                    );
                })
                .toList();

        return new Declarables(declarables);
    }
}
