package com.e2x.bigcommerce.routinefinder.antlr;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingErrorReporter implements ParsingErrorListener {
    @Override
    public void error(SyntaxError syntaxError) {
        log.error(syntaxError.getMsg());
    }
}
