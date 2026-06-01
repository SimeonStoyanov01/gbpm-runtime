package cs.rug.gbpmruntime.processregistry.application.bpmn4es;

import java.util.List;

public interface Bpmn4esKeiAnnotationParser {

    List<ElementKeiAnnotations> parse(byte[] bpmnXml);
}
