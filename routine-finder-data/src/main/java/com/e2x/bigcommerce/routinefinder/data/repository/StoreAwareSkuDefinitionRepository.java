package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.data.SkuDefinition;
import com.e2x.bigcommerce.routinefinder.data.SkuDefinitionRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class StoreAwareSkuDefinitionRepository implements SkuDefinitionRepository {
    private static final String SKU_DEFINITION_RESOURCE = "sku-definition";

    @Override
    public Optional<SkuDefinition> findBy(String storeId, String name) {
        var resourceBundle = ResourceBundle.getBundle(SKU_DEFINITION_RESOURCE, new Locale(storeId, ""));
        var skuId = resourceBundle.getString(String.format("%s.sku-id", name.toLowerCase()));

        if (StringUtils.isEmpty(skuId)) {
            return Optional.empty();
        }

        return Optional.of(new SkuDefinition(UUID.randomUUID().toString(), skuId, name.toLowerCase()));
    }

    @Override
    public void save(SkuDefinition skuDefinition) {
        throw new NotImplementedException();
    }

}
