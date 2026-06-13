package cs.rug.gbpmruntime.processregistry.application.out.bpmn4es;

import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.ElementKeiAnnotations;

import java.util.List;

public interface Bpmn4esKeiAnnotationParser {

    List<ElementKeiAnnotations> parse(byte[] bpmnXml);
}
