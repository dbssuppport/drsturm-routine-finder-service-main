package com.e2x.bigcommerce.routinefinder.enquiry.graph;

import java.util.Optional;

public interface QuestionNodeRepository {
    Optional<Object> findNodeBy(String questionId);
}
