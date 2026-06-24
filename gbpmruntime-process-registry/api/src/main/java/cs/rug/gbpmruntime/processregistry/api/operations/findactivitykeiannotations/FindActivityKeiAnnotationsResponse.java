package cs.rug.gbpmruntime.processregistry.api.operations.findactivitykeiannotations;

import cs.rug.gbpmruntime.processregistry.api.base.ProcessorResponse;
import cs.rug.gbpmruntime.processregistry.api.model.KeiAnnotationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindActivityKeiAnnotationsResponse implements ProcessorResponse {
    private List<KeiAnnotationModel> bpmn4esKeiAnnotations;
}
