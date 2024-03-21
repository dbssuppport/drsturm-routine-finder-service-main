package com.e2x.bigcommerce.routinefinder.antlr;

import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderBaseListener;
import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderLexer;
import com.e2x.bigcommerce.routinefinder.antlr.generated.RoutineFinderParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ConditionExpressionSyntaxValidator {

    public void validate(String expression, ParsingErrorListener parsingErrorListener) {
        CharStream inputStream = CharStreams.fromString(expression);
        var lexer = new RoutineFinderLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        var errorListener = new SyntaxErrorListener(parsingErrorListener);
        var parser = new RoutineFinderParser(tokens);
        parser.addErrorListener(errorListener);

        ParseTree parseTree = parser.start();

        var listener = new RoutineFinderBaseListener();
        ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(listener, parseTree);
    }
}
