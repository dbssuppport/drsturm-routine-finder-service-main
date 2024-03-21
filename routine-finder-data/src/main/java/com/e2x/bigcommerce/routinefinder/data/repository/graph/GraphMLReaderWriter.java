package com.e2x.bigcommerce.routinefinder.data.repository.graph;

import com.e2x.bigcommerce.routinefinder.data.repository.RoutineGraphReaderWriter;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Edge;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.InMemoryRoutineGraph;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexType;
import com.google.common.collect.Maps;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.graphml.GraphMLExporter;
import org.jgrapht.nio.graphml.GraphMLImporter;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.*;

public class GraphMLReaderWriter implements RoutineGraphReaderWriter {
    private static final String TYPE = "type";
    private static final String ID = "ID";
    private static final String EXPR = "expr";
    private static final String CODE = "code";
    private static final String STEPS = "steps";
    private static final String PROGRESS = "progress";
    private static final String QUESTION_ID = "questionId";

    @Override
    public RoutineGraph read(Reader reader) {
        GraphMLImporter<Vertex, Edge> importer = new GraphMLImporter<>();

        importer.setVertexFactory(s -> new Vertex(Long.parseLong(s)));
        importer.addVertexAttributeConsumer((vertexStringPair, attribute) -> {
            var attributeKey = vertexStringPair.getSecond();

            if (attributeKey.equals(TYPE)) {
                vertexStringPair.getFirst().addAttribute(attributeKey, VertexType.valueOf(attribute.getValue()));
            } else {
                if (!attributeKey.equals(ID)) {
                    vertexStringPair.getFirst().addAttribute(attributeKey, attribute.getValue());
                }
            }
        });

        var routineGraph = new InMemoryRoutineGraph();
        importer.importGraph(routineGraph, reader);

        return routineGraph;
    }

    @Override
    public void write(Writer writer, RoutineGraph graph) {
        GraphMLExporter<Vertex, Edge> exporter = new GraphMLExporter<>(v -> String.valueOf(v.getId()));
        exporter.setVertexAttributeProvider(this::getAttributesFor);

        exporter.registerAttribute(TYPE, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(CODE, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(EXPR, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(STEPS, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute(PROGRESS, GraphMLExporter.AttributeCategory.NODE, AttributeType.INT);
        exporter.registerAttribute(QUESTION_ID, GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);

        exporter.exportGraph(graph, writer);
    }

    private Map<String, Attribute> getAttributesFor(Vertex v) {
        Map<String, Attribute> attributeMap = Maps.newHashMap();

        attributeMap.put(TYPE, createAttributeFor(v.getVertexType().toString()));

        switch (v.getVertexType()) {
            case OPTION_VALUE:
                attributeMap.put(CODE, createAttributeFor(codeFor(v)));
                break;
            case CONDITION:
                attributeMap.put(EXPR, createAttributeFor(expressionFor(v)));
                break;
            case ROUTINE:
                attributeMap.put(STEPS, createAttributeFor(stepsFor(v)));
                break;
            case QUESTION:
                attributeMap.put(QUESTION_ID, createAttributeFor(questionIdFor(v)));
                attributeMap.put(PROGRESS, createAttributeFor(progressFor(v)));
                break;
            case OPTIONS:
            case OTHER:
                break;
        }

        return attributeMap;
    }

    private Attribute createAttributeFor(String value) {
        return DefaultAttribute.createAttribute(value);
    }

    private Attribute createAttributeFor(Integer value) {
        return DefaultAttribute.createAttribute(value);
    }
}
