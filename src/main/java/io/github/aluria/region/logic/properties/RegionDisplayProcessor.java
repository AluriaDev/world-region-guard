package io.github.aluria.region.logic.properties;

import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.logic.processor.RegionStringProperty;
import lombok.NonNull;

public final class RegionDisplayProcessor extends RegionStringProperty {

    public RegionDisplayProcessor() {
        super("displayName");
    }

    @Override
    public void processProperty(@NonNull RegionObject regionObject, @NonNull String value) {
        regionObject.setDisplayName(value);
    }
}
