package cs.rug.mockoperationalservice.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MockExecutionRunRecord {
    private String bpmnElementId;
    private String objectType;
    private String material;
    private String resourceName;
    private Double timeUsed;
    private String unit;
}
