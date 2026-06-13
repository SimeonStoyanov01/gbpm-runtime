package cs.rug.gbpmruntime.processregistry.application.extraction;

import org.junit.jupiter.api.Test;

import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.KeiMetadata;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.ElementKeiAnnotations;
import cs.rug.gbpmruntime.processregistry.infrastructure.xml.DomBpmn4esKeiAnnotationParser;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Bpmn4esKeiMetadataParserTest {

    private static final String BPMN_NAMESPACE = "http://www.omg.org/spec/BPMN/20100524/MODEL";
    private static final String BPMNDI_NAMESPACE = "http://www.omg.org/spec/BPMN/20100524/DI";
    private static final String DC_NAMESPACE = "http://www.omg.org/spec/DD/20100524/DC";
    private static final String BPMN4ES_NAMESPACE = "https://github.com/michel-medema/BPMN4ES";
    private static final String ZEEBE_NAMESPACE = "http://camunda.org/schema/zeebe/1.0";

    private final DomBpmn4esKeiAnnotationParser parser = new DomBpmn4esKeiAnnotationParser();

    @Test
    void parsesCamundaStyleBpmnWithBpmnPrefix() {
        List<ElementKeiAnnotations> metadata = parser.parse(xml("""
                <bpmn:definitions xmlns:bpmn="%s" xmlns:bpmn4es="%s">
                  <bpmn:process id="Process_1">
                    <bpmn:serviceTask id="Activity_0tw2fu0" name="Check inventory">
                      <bpmn:extensionElements>
                        <bpmn4es:environmentalIndicators>
                          <bpmn4es:keyEnvironmentalIndicator id="renewable-energy" unit="kwh" targetValue="1" icon="sunny" />
                        </bpmn4es:environmentalIndicators>
                      </bpmn:extensionElements>
                    </bpmn:serviceTask>
                  </bpmn:process>
                </bpmn:definitions>
                """.formatted(BPMN_NAMESPACE, BPMN4ES_NAMESPACE)));

        assertThat(metadata).hasSize(1);
        ElementKeiAnnotations mapping = metadata.getFirst();
        assertThat(mapping.getBpmnElementId()).isEqualTo("Activity_0tw2fu0");
        assertThat(mapping.getKeiMetadata()).hasSize(1);
        KeiMetadata keiDefinition = mapping.getKeiMetadata().getFirst();
        assertThat(keiDefinition.getId()).isEqualTo("renewable-energy");
        assertThat(keiDefinition.getUnit()).isEqualTo("kwh");
        assertThat(keiDefinition.getTargetValue()).isEqualTo("1");
        assertThat(keiDefinition.getIcon()).isEqualTo("sunny");
    }

    @Test
    void parsesBpmn2PrefixAndIgnoresZeebeIoMappings() {
        List<ElementKeiAnnotations> metadata = parser.parse(xml("""
                <bpmn2:definitions xmlns:bpmn2="%s" xmlns:bpmn4es="%s" xmlns:zeebe="%s">
                  <bpmn2:process id="Process_2">
                    <bpmn2:userTask id="Activity_UserTask">
                      <bpmn2:extensionElements>
                        <zeebe:ioMapping>
                          <zeebe:input source="=[{id:'ignored', unit:'kg'}]" target="keis" />
                        </zeebe:ioMapping>
                        <bpmn4es:environmentalIndicators>
                          <bpmn4es:keyEnvironmentalIndicator id="energy-consumption" unit="kwh" targetValue="5" icon="bolt" />
                        </bpmn4es:environmentalIndicators>
                      </bpmn2:extensionElements>
                    </bpmn2:userTask>
                  </bpmn2:process>
                </bpmn2:definitions>
                """.formatted(BPMN_NAMESPACE, BPMN4ES_NAMESPACE, ZEEBE_NAMESPACE)));

        assertThat(metadata).hasSize(1);
        assertThat(metadata.getFirst().getBpmnElementId()).isEqualTo("Activity_UserTask");
        assertThat(metadata.getFirst().getKeiMetadata())
                .extracting(KeiMetadata::getId)
                .containsExactly("energy-consumption");
    }

    @Test
    void parsesFlowableStyleDefaultBpmnNamespace() {
        List<ElementKeiAnnotations> metadata = parser.parse(xml("""
                <definitions xmlns="%s" xmlns:bpmn4es="%s">
                  <process id="Process_3">
                    <manualTask id="Activity_DefaultNamespace">
                      <extensionElements>
                        <bpmn4es:environmentalIndicators>
                          <bpmn4es:keyEnvironmentalIndicator id="waste" unit="kg" />
                        </bpmn4es:environmentalIndicators>
                      </extensionElements>
                    </manualTask>
                  </process>
                </definitions>
                """.formatted(BPMN_NAMESPACE, BPMN4ES_NAMESPACE)));

        assertThat(metadata).hasSize(1);
        KeiMetadata keiDefinition = metadata.getFirst().getKeiMetadata().getFirst();
        assertThat(keiDefinition.getId()).isEqualTo("waste");
        assertThat(keiDefinition.getUnit()).isEqualTo("kg");
        assertThat(keiDefinition.getTargetValue()).isNull();
        assertThat(keiDefinition.getIcon()).isNull();
    }

    @Test
    void parsesMultipleIndicatorsUnderOneEnvironmentalIndicatorsElement() {
        List<ElementKeiAnnotations> metadata = parser.parse(xml("""
                <bpmn:definitions xmlns:bpmn="%s" xmlns:bpmn4es="%s">
                  <bpmn:process id="Process_4">
                    <bpmn:scriptTask id="Activity_Multiple">
                      <bpmn:extensionElements>
                        <bpmn4es:environmentalIndicators>
                          <bpmn4es:keyEnvironmentalIndicator id="energy-consumption" unit="kwh" />
                          <bpmn4es:keyEnvironmentalIndicator id="water-consumption" unit="l" targetValue="10" />
                        </bpmn4es:environmentalIndicators>
                      </bpmn:extensionElements>
                    </bpmn:scriptTask>
                  </bpmn:process>
                </bpmn:definitions>
                """.formatted(BPMN_NAMESPACE, BPMN4ES_NAMESPACE)));

        assertThat(metadata).hasSize(1);
        assertThat(metadata.getFirst().getKeiMetadata())
                .extracting(KeiMetadata::getId)
                .containsExactly("energy-consumption", "water-consumption");
    }

    @Test
    void skipsMalformedIndicatorsWithoutId() {
        List<ElementKeiAnnotations> metadata = parser.parse(xml("""
                <bpmn:definitions xmlns:bpmn="%s" xmlns:bpmn4es="%s">
                  <bpmn:process id="Process_5">
                    <bpmn:task id="Activity_Malformed">
                      <bpmn:extensionElements>
                        <bpmn4es:environmentalIndicators>
                          <bpmn4es:keyEnvironmentalIndicator unit="kwh" />
                          <bpmn4es:keyEnvironmentalIndicator id="valid-indicator" unit="kg" />
                        </bpmn4es:environmentalIndicators>
                      </bpmn:extensionElements>
                    </bpmn:task>
                  </bpmn:process>
                </bpmn:definitions>
                """.formatted(BPMN_NAMESPACE, BPMN4ES_NAMESPACE)));

        assertThat(metadata).hasSize(1);
        assertThat(metadata.getFirst().getKeiMetadata())
                .extracting(KeiMetadata::getId)
                .containsExactly("valid-indicator");
    }

    @Test
    void returnsEmptyMappingsWhenNoBpmn4esIndicatorsExist() {
        List<ElementKeiAnnotations> metadata = parser.parse(xml("""
                <bpmn:definitions xmlns:bpmn="%s">
                  <bpmn:process id="Process_6">
                    <bpmn:serviceTask id="Activity_NoIndicators" />
                  </bpmn:process>
                </bpmn:definitions>
                """.formatted(BPMN_NAMESPACE)));

        assertThat(metadata).isEmpty();
    }

    @Test
    void ignoresBpmnDiagramElements() {
        List<ElementKeiAnnotations> metadata = parser.parse(xml("""
                <bpmn:definitions xmlns:bpmn="%s" xmlns:bpmn4es="%s" xmlns:bpmndi="%s" xmlns:dc="%s">
                  <bpmn:process id="Process_7">
                    <bpmn:serviceTask id="Activity_Valid">
                      <bpmn:extensionElements>
                        <bpmn4es:environmentalIndicators>
                          <bpmn4es:keyEnvironmentalIndicator id="valid" unit="kg" />
                        </bpmn4es:environmentalIndicators>
                      </bpmn:extensionElements>
                    </bpmn:serviceTask>
                  </bpmn:process>
                  <bpmndi:BPMNDiagram id="Diagram_1">
                    <bpmndi:BPMNPlane id="Plane_1">
                      <bpmndi:BPMNShape id="Shape_1" bpmnElement="Activity_Valid">
                        <dc:Bounds x="1" y="2" width="3" height="4" />
                      </bpmndi:BPMNShape>
                    </bpmndi:BPMNPlane>
                  </bpmndi:BPMNDiagram>
                </bpmn:definitions>
                """.formatted(BPMN_NAMESPACE, BPMN4ES_NAMESPACE, BPMNDI_NAMESPACE, DC_NAMESPACE)));

        assertThat(metadata).hasSize(1);
        assertThat(metadata.getFirst().getBpmnElementId()).isEqualTo("Activity_Valid");
    }

    private byte[] xml(String xml) {
        return xml.getBytes(StandardCharsets.UTF_8);
    }
}
