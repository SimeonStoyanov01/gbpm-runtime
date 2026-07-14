package cs.rug.monitoringresultsservice.infrastructure.rest;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsOperation;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MonitoringRecordsController {

    private final FindMonitoringRecordsOperation findMonitoringRecordsOperation;

    @GetMapping("/api/monitoring/records")
    public ResponseEntity<List<MonitoringRecord>> findRecords(
            @RequestParam(required = false) Long processInstanceKey,
            @RequestParam(required = false) Long processDefinitionKey,
            @RequestParam(required = false) String bpmnProcessId,
            @RequestParam(required = false) String evaluationStatus
    ) {
        List<MonitoringRecord> records = findMonitoringRecordsOperation
                .process(FindMonitoringRecordsRequest
                        .builder()
                        .processInstanceKey(processInstanceKey)
                        .processDefinitionKey(processDefinitionKey)
                        .bpmnProcessId(bpmnProcessId)
                        .evaluationStatus(evaluationStatus)
                        .build());

        return ResponseEntity.ok(records);
    }
}
