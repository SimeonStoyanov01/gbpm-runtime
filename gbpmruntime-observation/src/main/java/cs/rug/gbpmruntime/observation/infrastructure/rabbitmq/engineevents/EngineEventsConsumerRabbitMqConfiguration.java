package cs.rug.gbpmruntime.observation.infrastructure.rabbitmq.engineevents;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
        List<Declarable> declarables = new ArrayList<>();

        for (EngineEventsRabbitMqProperties.Binding binding : properties.getBindings()) {
            TopicExchange exchange = new TopicExchange(binding.getExchangeName(), true, false);
            declarables.add(exchange);
            declarables.add(BindingBuilder
                    .bind(engineEventsQueue)
                    .to(exchange)
                    .with(binding.getRoutingKey()));
        }

        return new Declarables(declarables);
    }
}
