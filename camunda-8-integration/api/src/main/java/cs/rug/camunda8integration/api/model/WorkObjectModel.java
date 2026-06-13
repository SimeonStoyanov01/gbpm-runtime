package cs.rug.camunda8integration.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkObjectModel {
    private String objectId;
    private String type;
    private String material;
}
