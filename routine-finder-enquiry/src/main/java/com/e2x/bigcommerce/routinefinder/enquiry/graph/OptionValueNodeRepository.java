package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import java.util.Optional;

public interface OptionValueNodeRepository {
    Optional<Vertex> findNodeBy(String code);
}
