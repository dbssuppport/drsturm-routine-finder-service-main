package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import java.util.Optional;

public interface SkuNodeRepository {
    Optional<SkuNode> findNodeBy(String id);
}
