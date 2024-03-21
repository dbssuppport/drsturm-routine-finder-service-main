package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import com.e2x.bigcommerce.routinefinder.antlr.RoutineFinderExpressionInterpreter;
import com.e2x.bigcommerce.routinefinder.enquiry.Questionnaire;
import com.google.common.collect.Maps;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
public class Vertex {
    private static final String TYPE = "type";
    private final long id;
    private final Map<String, Object> attributes = Maps.newHashMap();

    public Vertex() {
        this(VertexType.OTHER);
    }

    public Vertex(long id) {
        this(id, VertexType.OTHER);
    }

    public Vertex(VertexType vertexType) {
        this(IdGenerator.nextLong(), vertexType);
    }

    public Vertex(long id, VertexType vertexType) {
        this.id = id;
        attributes.put(TYPE, vertexType);
    }

    public boolean isTriggered(Questionnaire questionnaire) {
        if (VertexType.CONDITION.equals(getVertexType())) {
            var interpreter = new RoutineFinderExpressionInterpreter();
            var triggered = interpreter.evaluate(VertexUtils.expressionFor(this), questionnaire);

            if (log.isDebugEnabled()) {
                log.debug(VertexUtils.expressionFor(this) + " = " + triggered);
            }

            return triggered;
        }

        return true;
    }

    public VertexType getVertexType() {
        return (VertexType) attributes.get(TYPE);
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }
}
