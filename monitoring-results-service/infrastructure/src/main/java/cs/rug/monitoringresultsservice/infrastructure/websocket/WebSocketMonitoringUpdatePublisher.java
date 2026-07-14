package cs.rug.monitoringresultsservice.infrastructure.websocket;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.application.out.MonitoringUpdatePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketMonitoringUpdatePublisher implements MonitoringUpdatePublisher {

    private static final String CALCULATIONS_TOPIC = "/topic/monitoring/calculations";
    private static final String EVALUATIONS_TOPIC = "/topic/monitoring/evaluations";
    private static final String VIOLATIONS_TOPIC = "/topic/monitoring/violations";

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publishCalculationUpdate(MonitoringRecord record) {
        messagingTemplate.convertAndSend(CALCULATIONS_TOPIC, record);
    }

    @Override
    public void publishEvaluationUpdate(MonitoringRecord record) {
        messagingTemplate.convertAndSend(EVALUATIONS_TOPIC, record);
    }

    @Override
    public void publishViolationUpdate(MonitoringRecord record) {
        messagingTemplate.convertAndSend(VIOLATIONS_TOPIC, record);
    }
}
