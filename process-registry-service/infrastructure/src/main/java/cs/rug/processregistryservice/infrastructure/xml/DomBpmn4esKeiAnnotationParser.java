package cs.rug.processregistryservice.infrastructure.xml;

import cs.rug.processregistryservice.api.exceptions.InvalidProcessDefinitionException;
import cs.rug.processregistryservice.api.model.ElementKeiAnnotations;
import cs.rug.processregistryservice.api.model.KeiAnnotation;
import cs.rug.processregistryservice.application.out.bpmn4es.Bpmn4esKeiAnnotationParser;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DomBpmn4esKeiAnnotationParser implements Bpmn4esKeiAnnotationParser {

    private static final String BPMN_NAMESPACE = "http://www.omg.org/spec/BPMN/20100524/MODEL";
    private static final String BPMN4ES_NAMESPACE = "https://github.com/michel-medema/BPMN4ES";
    private static final String ENVIRONMENTAL_INDICATORS = "environmentalIndicators";
    private static final String KEY_ENVIRONMENTAL_INDICATOR = "keyEnvironmentalIndicator";

    private static final List<String> DIAGRAM_LOCAL_NAMES = List.of(
            "BPMNDiagram",
            "BPMNPlane",
            "BPMNShape",
            "BPMNEdge",
            "Bounds",
            "waypoint"
    );

    @Override
    public List<ElementKeiAnnotations> parse(byte[] bpmnXml) {
        try {
            Document document = parseDocument(bpmnXml);

            return findElementKeiAnnotations(document);
        } catch (Exception exception) {
            throw new InvalidProcessDefinitionException(exception);
        }
    }

    private Document parseDocument(byte[] bpmnXml) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new ByteArrayInputStream(bpmnXml)));
        document.getDocumentElement().normalize();
        return document;
    }

    private List<ElementKeiAnnotations> findElementKeiAnnotations(Document document) {
        NodeList environmentalIndicatorNodes = document.getElementsByTagNameNS(BPMN4ES_NAMESPACE, ENVIRONMENTAL_INDICATORS);

        List<ElementKeiAnnotations> elementKeiAnnotations = new ArrayList<>();
        for (int i = 0; i < environmentalIndicatorNodes.getLength(); i++) {
            Node environmentalIndicatorNode = environmentalIndicatorNodes.item(i);
            if (!(environmentalIndicatorNode instanceof Element environmentalIndicatorElement)) {
                continue;
            }

            findElementKeiAnnotation(environmentalIndicatorElement).ifPresent(elementKeiAnnotations::add);
        }

        return elementKeiAnnotations;
    }

    private Optional<ElementKeiAnnotations> findElementKeiAnnotation(Element environmentalIndicatorElement) {
        Optional<Element> bpmnParentElement = findNearestBpmnParentWithId(environmentalIndicatorElement);
        if (bpmnParentElement.isEmpty()) {
            return Optional.empty();
        }

        List<KeiAnnotation> keiAnnotations = findKeiAnnotations(environmentalIndicatorElement);
        if (keiAnnotations.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ElementKeiAnnotations.builder()
                .bpmnElementId(bpmnParentElement.get().getAttribute("id"))
                .elementName(findAttributeValue(bpmnParentElement.get(), "name").orElse(null))
                .elementType(bpmnParentElement.get().getLocalName())
                .keiAnnotations(keiAnnotations)
                .build());
    }

    private Optional<Element> findNearestBpmnParentWithId(Node node) {
        Node currentNode = node.getParentNode();
        while (currentNode != null) {
            if (currentNode instanceof Element currentElement
                    && isBpmnElement(currentElement)
                    && !isDiagramElement(currentElement)
                    && !currentElement.getAttribute("id").isBlank()) {
                return Optional.of(currentElement);
            }

            currentNode = currentNode.getParentNode();
        }

        return Optional.empty();
    }

    private List<KeiAnnotation> findKeiAnnotations(Element environmentalIndicatorsElement) {
        NodeList childNodes = environmentalIndicatorsElement.getChildNodes();

        List<KeiAnnotation> keiAnnotations = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (!(childNode instanceof Element childElement)
                    || !isBpmn4esElement(childElement, KEY_ENVIRONMENTAL_INDICATOR)) {
                continue;
            }

            buildKeiAnnotation(childElement).ifPresent(keiAnnotations::add);
        }

        return keiAnnotations;
    }

    private Optional<KeiAnnotation> buildKeiAnnotation(Element childElement) {
        return findAttributeValue(childElement, "id")
                .map(id -> KeiAnnotation.builder()
                        .id(id)
                        .unit(findAttributeValue(childElement, "unit").orElse(null))
                        .targetValue(findAttributeValue(childElement, "targetValue").orElse(null))
                        .icon(findAttributeValue(childElement, "icon").orElse(null))
                        .build());
    }

    private boolean isBpmnElement(Element element) {
        return BPMN_NAMESPACE.equals(element.getNamespaceURI());
    }

    private boolean isBpmn4esElement(Element element, String localName) {
        return BPMN4ES_NAMESPACE.equals(element.getNamespaceURI())
                && localName.equals(element.getLocalName());
    }

    private boolean isDiagramElement(Element element) {
        return DIAGRAM_LOCAL_NAMES.contains(element.getLocalName());
    }

    private Optional<String> findAttributeValue(Element element, String attributeName) {
        String value = element.getAttribute(attributeName);
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(value);
    }
}
