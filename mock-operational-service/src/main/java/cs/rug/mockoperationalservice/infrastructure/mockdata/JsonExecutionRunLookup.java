package cs.rug.mockoperationalservice.infrastructure.mockdata;

import cs.rug.mockoperationalservice.application.model.MockExecutionRunRecord;
import cs.rug.mockoperationalservice.application.out.ExecutionRunLookup;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JsonExecutionRunLookup implements ExecutionRunLookup {

    private static final String MOCK_DATA_LOCATION = "mock-execution-runs.json";

    private final ObjectMapper objectMapper;

    private List<MockExecutionRunRecord> records;

    @PostConstruct
    void loadRecords() {
        try {
            MockExecutionRunRecord[] loadedRecords = objectMapper.readValue(
                    new ClassPathResource(MOCK_DATA_LOCATION).getInputStream(),
                    MockExecutionRunRecord[].class
            );
            this.records = Arrays.asList(loadedRecords);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load mock execution run data.", exception);
        }
    }

    @Override
    public Optional<MockExecutionRunRecord> findExecutionRun(
            String bpmnElementId,
            String objectType,
            String material
    ) {
        return records
                .stream()
                .filter(record -> record.getBpmnElementId().equals(bpmnElementId))
                .filter(record -> record.getObjectType().equals(objectType))
                .filter(record -> record.getMaterial().equals(material))
                .findFirst();
    }
}
