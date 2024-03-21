package com.e2x.bigcommerce.routinefinder.cli;

import java.io.IOException;
import java.io.Reader;

public interface RoutineGraphConfigurationReader {
    /**
     * invoke vertex listener when a new vertex has been read from the reader input stream.
     * @param reader reader of vertices
     * @param vertexReaderListener listener
     * @throws IOException
     */
    void read(Reader reader, VertexReaderListener vertexReaderListener) throws IOException;
}
