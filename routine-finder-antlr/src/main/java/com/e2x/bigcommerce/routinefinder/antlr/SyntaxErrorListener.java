package com.e2x.bigcommerce.routinefinder.antlr;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.List;

@Slf4j
@Getter
class SyntaxErrorListener extends BaseErrorListener {
    private final List<SyntaxError> errors = Lists.newArrayList();
    private final ParsingErrorListener parsingErrorListener;

    public SyntaxErrorListener(ParsingErrorListener parsingErrorListener) {
        this.parsingErrorListener = parsingErrorListener;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
        var syntaxError = new SyntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);

        parsingErrorListener.error(syntaxError);

        log.warn("an error was reported: " + syntaxError.getMsg());
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }
}
