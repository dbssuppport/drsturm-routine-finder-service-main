package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;

import java.io.Reader;
import java.io.Writer;

public interface RoutineGraphReaderWriter {
    RoutineGraph read(Reader reader);
    void write(Writer writer, RoutineGraph graph);
}
