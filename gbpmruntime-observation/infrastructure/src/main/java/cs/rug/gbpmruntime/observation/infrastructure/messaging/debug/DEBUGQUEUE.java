package cs.rug.gbpmruntime.observation.infrastructure.messaging.debug;

import cs.rug.gbpmruntime.observation.infrastructure.messaging.outbound.keiobservations.KeiObservationEventsRabbitMqProperties;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DEBUGQUEUE {

    private static final String KEI_OBSERVATION_CREATED_DEBUG_QUEUE = "debug.kei.observation.created";
    private static final String KEI_CALCULATION_REQUESTED_DEBUG_QUEUE = "debug.kei.calculation.requested";

    @Bean
    public Queue keiObservationCreatedDebugQueue() {
        return new Queue(KEI_OBSERVATION_CREATED_DEBUG_QUEUE, true);
    }

    @Bean
    public Binding keiObservationCreatedDebugBinding(
            Queue keiObservationCreatedDebugQueue,
            TopicExchange keiObservationEventsExchange,
            KeiObservationEventsRabbitMqProperties properties
    ) {
        return BindingBuilder
                .bind(keiObservationCreatedDebugQueue)
                .to(keiObservationEventsExchange)
                .with(properties.getObservationCreatedRoutingKey());
    }

    @Bean
    public Queue keiCalculationRequestedDebugQueue() {
        return new Queue(KEI_CALCULATION_REQUESTED_DEBUG_QUEUE, true);
    }

    @Bean
    public Binding keiCalculationRequestedDebugBinding(
            Queue keiCalculationRequestedDebugQueue,
            TopicExchange keiObservationEventsExchange,
            KeiObservationEventsRabbitMqProperties properties
    ) {
        return BindingBuilder
                .bind(keiCalculationRequestedDebugQueue)
                .to(keiObservationEventsExchange)
                .with(properties.getCalculationRequestedRoutingKey());
    }
}
