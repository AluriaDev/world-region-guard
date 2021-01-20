package io.github.aluria.region.command.property;

import io.github.aluria.region.command.parser.type.RegionIntegerParser;
import io.github.aluria.region.entity.RegionObject;
import lombok.NonNull;

public final class RegionPriorityParser extends RegionIntegerParser {

    public RegionPriorityParser() {
        super("priority");
    }

    @Override
    public void processProperty(@NonNull RegionObject regionObject, @NonNull Integer value) {
        regionObject.setPriority(value);
    }
}
