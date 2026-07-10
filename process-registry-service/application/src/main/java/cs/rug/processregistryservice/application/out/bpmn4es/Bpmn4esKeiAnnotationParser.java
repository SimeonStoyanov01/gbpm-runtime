package cs.rug.processregistryservice.application.out.bpmn4es;

import cs.rug.processregistryservice.application.model.bpmn4es.ElementKeiAnnotations;

import java.util.List;

public interface Bpmn4esKeiAnnotationParser {

    List<ElementKeiAnnotations> parse(byte[] bpmnXml);
}
