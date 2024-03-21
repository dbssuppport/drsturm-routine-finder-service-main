package com.e2x.bigcommerce.routinefinder.antlr;

import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderBaseListener;
import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderLexer;
import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderParser;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.List;

public class ConditionExpressionWalker {

    public static void walk(String expression, RoutineFinderBaseListener routineFinderListener) {
        var parser = createParserFor(expression);
        List<SyntaxError> errors = Lists.newArrayList();

        var errorListener = new SyntaxErrorListener(errors::add);
        parser.addErrorListener(errorListener);

        var walker = new ParseTreeWalker();
        walker.walk(routineFinderListener, parser.start());

        if (errors.size() > 0) {
            throw new ConditionExpressionException(errors);
        }
    }

    private static RoutineFinderParser createParserFor(String text) {
        var lexer = new RoutineFinderLexer(CharStreams.fromString(text));
        var tokenStream = new CommonTokenStream(lexer);

        return new RoutineFinderParser(tokenStream);
    }
}
