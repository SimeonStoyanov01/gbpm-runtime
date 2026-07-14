package cs.rug.observationservice.infrastructure.messaging.outbound.keiobservations;

import cs.rug.observationservice.api.events.keicalculationrequested.KeiCalculationRequestedEvent;
import cs.rug.observationservice.application.out.KeiCalculationRequestedEventPublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class PublishKeiCalculationRequestedEventRabbitMqPublisher
        implements KeiCalculationRequestedEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final KeiCalculationRequestEventsRabbitMqProperties rabbitMqProperties;

    @PostConstruct
    void configureUnroutableMessageLogging() {
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returned -> log.warn(
                "KEI calculation request was not routed to a calculator: routingKey={}, reason={}",
                returned.getRoutingKey(),
                returned.getReplyText()
        ));
    }

    @Override
    public void publish(KeiCalculationRequestedEvent event) {
        rabbitTemplate.send(
                rabbitMqProperties.getExchangeName(),
                routingKeyFor(event),
                buildJsonMessage(event)
        );
    }

    private String routingKeyFor(KeiCalculationRequestedEvent event) {
        return rabbitMqProperties.getCalculationRequestedRoutingKeyPrefix() + "." + event.getKei().getId();
    }

    private Message buildJsonMessage(KeiCalculationRequestedEvent event) {
        try {
            return MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(event))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to serialize KEI calculation requested event.", exception);
        }
    }
}
