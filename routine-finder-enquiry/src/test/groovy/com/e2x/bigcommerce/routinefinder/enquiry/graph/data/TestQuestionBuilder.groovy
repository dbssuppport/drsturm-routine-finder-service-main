package com.e2x.bigcommerce.routinefinder.enquiry.graph.data


import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.questionIdFor
import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newQuestionVertexFor

class TestQuestionBuilder {
    static final String AGE_ID = 'age'
    static final String SKIN_TYPE_ID = 'skinType'
    static final String SKIN_TONE_ID = 'skinTone'
    static final String SKIN_CONCERNS_ID = 'skinConcerns'

    static final Vertex AGE = newQuestionVertexFor('age')
    static final Vertex SKIN_TYPE = newQuestionVertexFor('skinType')
    static final Vertex SKIN_TONE = newQuestionVertexFor('skinTone')
    static final Vertex SKIN_CONCERNS = newQuestionVertexFor('skinConcerns')

    static Vertex clone(Vertex nodeToClone) {
        newQuestionVertexFor(questionIdFor(nodeToClone))
    }

}
