package cs.rug.gbpmruntime.processregistry.api.operations.findprocesskeiannotations;

import cs.rug.gbpmruntime.common.api.base.ProcessorResponse;
import cs.rug.gbpmruntime.processregistry.api.model.ElementKeiAnnotationsModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindProcessKeiAnnotationsResponse implements ProcessorResponse {
    private List<ElementKeiAnnotationsModel> bpmn4esElementKeiAnnotations;
}
