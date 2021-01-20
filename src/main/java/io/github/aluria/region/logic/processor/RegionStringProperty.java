package io.github.aluria.region.logic.processor;

import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.logic.RegionPropertyProcessor;
import lombok.NonNull;

public abstract class RegionStringProperty extends RegionPropertyProcessor<String> {

    public RegionStringProperty(@NonNull String identifier) {
        super(identifier);
    }

    @Override
    public String processRawProperty(@NonNull RegionObject regionObject, @NonNull String value) {
        return value;
    }
}
