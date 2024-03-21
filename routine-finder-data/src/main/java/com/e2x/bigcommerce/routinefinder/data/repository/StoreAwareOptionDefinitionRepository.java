package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.data.OptionDefinition;
import com.e2x.bigcommerce.routinefinder.data.OptionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.OptionValueNodeRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.graph.Vertex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.e2x.bigcommerce.routinefinder.enquiry.graph.VertexUtils.newOptionValueVertex;

@Slf4j
public class StoreAwareOptionDefinitionRepository implements OptionDefinitionRepository, OptionValueNodeRepository {
    private static final String BUNDLE_RESOURCE_NAME = "question-definition";
    private static final String DEFAULT = "default";

    @Override
    public void save(String storeId, OptionDefinition optionDefinition) {
        throw new NotImplementedException("not supported in current implementation");
    }

    @Override
    public Optional<OptionDefinition> findBy(String storeId, String questionId, String code) {
        try {
            var resourceBundle = ResourceBundle.getBundle(BUNDLE_RESOURCE_NAME, new Locale(storeId, ""));

            var optionCodeFullPath = String.format("%s.option.%s", questionId, code).replaceAll(" ", "-");

            var optionDefinition = new OptionDefinition();
            optionDefinition.setCode(resourceBundle.getString(String.format("%s.code", optionCodeFullPath)));
            optionDefinition.setText(resourceBundle.getString(String.format("%s.text", optionCodeFullPath)));

            return Optional.of(optionDefinition);
        } catch (MissingResourceException ex) {
            log.warn("could not find resource", ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Vertex> findNodeBy(String code) {
        return findBy(DEFAULT, "", code)
                .map(o -> newOptionValueVertex(o.getCode()));
    }
}
