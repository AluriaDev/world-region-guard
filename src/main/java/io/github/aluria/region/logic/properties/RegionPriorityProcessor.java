package io.github.aluria.region.logic.properties;

import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.logic.processor.RegionIntegerProperty;
import lombok.NonNull;

public final class RegionPriorityProcessor extends RegionIntegerProperty {

    public RegionPriorityProcessor() {
        super("priority");
    }

    @Override
    public void processProperty(@NonNull RegionObject regionObject, @NonNull Integer value) {
        regionObject.setPriority(value);
    }
}
