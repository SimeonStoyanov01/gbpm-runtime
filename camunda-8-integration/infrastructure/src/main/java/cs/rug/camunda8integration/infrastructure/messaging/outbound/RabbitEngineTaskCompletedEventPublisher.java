package cs.rug.camunda8integration.infrastructure.messaging.outbound;

import cs.rug.camunda8integration.api.events.enginetaskcompleted.EngineTaskCompletedEvent;
import cs.rug.camunda8integration.application.out.EngineTaskCompletedEventPublisher;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class RabbitEngineTaskCompletedEventPublisher implements EngineTaskCompletedEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String exchangeName;
    private final String routingKey;

    public RabbitEngineTaskCompletedEventPublisher(
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper,
            @Value("${camunda8integration.messaging.engine-task-completed.exchange-name}") String exchangeName,
            @Value("${camunda8integration.messaging.engine-task-completed.routing-key}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    @Override
    public void publish(EngineTaskCompletedEvent event) {
        rabbitTemplate.send(
                exchangeName,
                routingKey,
                buildJsonMessage(event)
        );
    }

    private Message buildJsonMessage(EngineTaskCompletedEvent event) {
        try {
            return MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(event))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to serialize engine task completed event.", exception);
        }
    }
}
