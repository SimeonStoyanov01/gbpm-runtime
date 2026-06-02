package cs.rug.gbpmruntime.engineintegration.application.processors.startprocess;

import cs.rug.gbpmruntime.engineintegration.api.exceptions.UnsupportedWorkflowEngineException;
import cs.rug.gbpmruntime.engineintegration.api.operations.startprocess.StartProcessInEngineRequest;
import cs.rug.gbpmruntime.engineintegration.api.operations.startprocess.StartProcessInEngineResponse;
import cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8.Camunda8ProcessInstanceClient;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StartProcessInEngineProcessorTest {

    private final Camunda8ProcessInstanceClient camunda8ProcessInstanceClient = mock(Camunda8ProcessInstanceClient.class);
    private final StartProcessInEngineProcessor processor = new StartProcessInEngineProcessor(camunda8ProcessInstanceClient);

    @Test
    void rejectsUnsupportedTargetEngine() {
        StartProcessInEngineRequest request = StartProcessInEngineRequest
                .builder()
                .processDefinitionKey("2251799813685373")
                .targetEngine("ACTIVITI")
                .build();

        assertThatThrownBy(() -> processor.process(request))
                .isInstanceOf(UnsupportedWorkflowEngineException.class);
    }

    @Test
    void delegatesCamunda8StartWithProcessDefinitionKeyAndVariables() {
        Map<String, Object> variables = Map.of("orderId", "12345", "amount", 100.0);
        StartProcessInEngineResponse expectedResponse = StartProcessInEngineResponse
                .builder()
                .processDefinitionKey("2251799813685373")
                .bpmnProcessId("order-process")
                .version(1)
                .processInstanceKey("2251799813689999")
                .tenantId("<default>")
                .build();

        when(camunda8ProcessInstanceClient.start("2251799813685373", variables)).thenReturn(expectedResponse);

        StartProcessInEngineResponse response = processor.process(StartProcessInEngineRequest
                .builder()
                .processDefinitionKey("2251799813685373")
                .targetEngine("CAMUNDA_8")
                .variables(variables)
                .build());

        assertThat(response).isSameAs(expectedResponse);
        verify(camunda8ProcessInstanceClient).start("2251799813685373", variables);
    }
}
