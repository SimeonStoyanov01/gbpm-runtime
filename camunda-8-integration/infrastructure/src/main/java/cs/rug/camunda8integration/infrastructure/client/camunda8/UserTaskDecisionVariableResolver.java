package cs.rug.camunda8integration.infrastructure.client.camunda8;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.search.response.UserTask;
import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.instance.ConditionExpression;
import io.camunda.zeebe.model.bpmn.instance.ExclusiveGateway;
import io.camunda.zeebe.model.bpmn.instance.SequenceFlow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserTaskDecisionVariableResolver {

    private static final Pattern TRUE_CONDITION = Pattern.compile(
            "^\\s*=\\s*([A-Za-z_][A-Za-z0-9_]*)\\s*=\\s*true\\s*$"
    );

    private final CamundaClient camundaClient;
    private final Map<Long, Map<String, String>> processDecisionVariables = new ConcurrentHashMap<>();

    public String resolve(UserTask userTask) {
        return processDecisionVariables
                .computeIfAbsent(userTask.getProcessDefinitionKey(), this::loadDecisionVariables)
                .get(userTask.getElementId());
    }

    private Map<String, String> loadDecisionVariables(Long processDefinitionKey) {
        String bpmnXml = camundaClient
                .newProcessDefinitionGetXmlRequest(processDefinitionKey)
                .send()
                .join();
        BpmnModelInstance model = Bpmn.readModelFromStream(new ByteArrayInputStream(
                bpmnXml.getBytes(StandardCharsets.UTF_8)
        ));

        return model
                .getModelElementsByType(io.camunda.zeebe.model.bpmn.instance.UserTask.class)
                .stream()
                .filter(task -> task.getOutgoing().size() == 1)
                .filter(task -> task.getOutgoing().iterator().next().getTarget() instanceof ExclusiveGateway)
                .flatMap(task -> decisionVariable(task)
                        .stream()
                        .map(variable -> Map.entry(task.getId(), variable)))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Optional<String> decisionVariable(io.camunda.zeebe.model.bpmn.instance.UserTask task) {
        ExclusiveGateway gateway = (ExclusiveGateway) task.getOutgoing().iterator().next().getTarget();
        if (gateway.getDefault() == null) {
            return Optional.empty();
        }

        List<String> variables = gateway
                .getOutgoing()
                .stream()
                .filter(flow -> flow != gateway.getDefault())
                .map(SequenceFlow::getConditionExpression)
                .filter(condition -> condition != null)
                .map(ConditionExpression::getTextContent)
                .map(TRUE_CONDITION::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group(1))
                .toList();
        return variables.size() == 1 ? Optional.of(variables.getFirst()) : Optional.empty();
    }
}
