package com.e2x.bigcommerce.routinefinder.cli;

import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;

public interface VertexReaderListener {
    void onVertexRead(Vertex vertex, long parentVertexId);
    void onVertexReadFinished();
}
