package cs.rug.camunda8integration.infrastructure.rabbitmq;

import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventOperation;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventRequest;
import cs.rug.camunda8integration.api.operations.publishenginetaskcompleted.PublishEngineTaskCompletedEventResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class EngineTaskCompletedEventPublisher implements PublishEngineTaskCompletedEventOperation {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String exchangeName;
    private final String routingKey;

    public EngineTaskCompletedEventPublisher(
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
    public PublishEngineTaskCompletedEventResponse process(PublishEngineTaskCompletedEventRequest request) {
        rabbitTemplate.send(
                exchangeName,
                routingKey,
                buildJsonMessage(request)
        );

        return PublishEngineTaskCompletedEventResponse
                .builder()
                .eventId(request.getEvent().getEventId())
                .published(true)
                .build();
    }

    private Message buildJsonMessage(PublishEngineTaskCompletedEventRequest request) {
        try {
            return MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(request.getEvent()))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to serialize engine task completed event.", exception);
        }
    }
}
