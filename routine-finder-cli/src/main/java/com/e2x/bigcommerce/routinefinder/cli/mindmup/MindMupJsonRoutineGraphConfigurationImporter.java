package com.e2x.bigcommerce.routinefinder.cli.mindmup;

import com.e2x.bigcommerce.routinefinder.enquiry.graph.IdGenerator;
import com.e2x.bigcommerce.routinefinder.cli.RoutineGraphConfigurationReader;
import com.e2x.bigcommerce.routinefinder.cli.VertexReaderListener;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Queues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.Deque;

@Slf4j
@Component
public class MindMupJsonRoutineGraphConfigurationImporter implements RoutineGraphConfigurationReader {
    private final RoutineGraphVertexFactory routineGraphVertexFactory;

    public MindMupJsonRoutineGraphConfigurationImporter(RoutineGraphVertexFactory routineGraphVertexFactory) {
        this.routineGraphVertexFactory = routineGraphVertexFactory;
    }

    @Override
    public void read(Reader reader, VertexReaderListener vertexReaderListener) throws IOException {
        log.info("reading graph configuration...");
        Deque<MindMupJsonNode> parsedNodeQueue = Queues.newArrayDeque();

        JsonFactory jsonfactory = new JsonFactory();
        var parser = jsonfactory.createParser(reader);

        var token = parser.nextToken();

        MindMupJsonNode currentNode = null;
        MindMupJsonNode parentNode = null;

        String currentField = null;

        while (token != null) {
            switch (token) {
                case START_OBJECT:
                    if (!Strings.isNullOrEmpty(parser.getCurrentName()) && parser.getCurrentName().equals("attr")) {
                        parser.skipChildren();
                    } else {
                        if (parentNode == null) {
                            currentNode = createRootNode();
                        } else {
                            currentNode = createNode(parentNode);
                        }

                        parsedNodeQueue.add(currentNode);
                    }
                    break;
                case END_OBJECT:
                    var node = parsedNodeQueue.pollLast();

                    if (shouldNotify(node)) {
                        var graphNode = routineGraphVertexFactory.createVertexFor(node);
                        long parentNodeId = parentNode != null ? parentNode.getId() : 0L;
                        vertexReaderListener.onVertexRead(graphNode, parentNodeId);
                    }

                    currentNode = parsedNodeQueue.peekLast();

                    if (currentNode != null) {
                        parentNode = currentNode.getParentNode();
                    }

                    break;
                case FIELD_NAME:
                    currentField = parser.getCurrentName();

                    if (currentField.equals("ideas")) {
                        parentNode = currentNode;
                    }

                    break;
                case VALUE_STRING:
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                case VALUE_TRUE:
                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_EMBEDDED_OBJECT:
                    if (currentNode != null) {
                        currentNode.addAttribute(currentField, parser.getValueAsString());
                    }

                    currentField = null;
                    break;
                case NOT_AVAILABLE:
                case START_ARRAY:
                case END_ARRAY:
                    break;
            }

            token = parser.nextToken();
        }

        vertexReaderListener.onVertexReadFinished();
    }

    private static MindMupJsonNode createNode(MindMupJsonNode parentNode) {
        return new MindMupJsonNode(IdGenerator.nextLong(), parentNode);
    }

    private static MindMupJsonNode createRootNode() {
        return new MindMupJsonNode(-1L, null);
    }

    private boolean shouldNotify(MindMupJsonNode node) {
        if (node == null || node.getId() == -1L) {
            return false;
        }

        return !node.getAttributes().isEmpty();
    }

}
