package cs.rug.observationservice.infrastructure.messaging.outbound.keiobservations;

import cs.rug.observationservice.api.events.keiobservationcreated.KeiObservationEvent;
import cs.rug.observationservice.application.out.KeiObservationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class PublishKeiObservationEventRabbitMqPublisher implements KeiObservationEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final KeiObservationEventsRabbitMqProperties rabbitMqProperties;

    @Override
    public void publish(KeiObservationEvent event) {
        rabbitTemplate.send(
                rabbitMqProperties.getExchangeName(),
                rabbitMqProperties.getObservationCreatedRoutingKey(),
                buildJsonMessage(event)
        );
    }

    private Message buildJsonMessage(KeiObservationEvent event) {
        try {
            return MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(event))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to serialize KEI observation event.", exception);
        }
    }
}
