package io.github.aluria.region.command.property;

import io.github.aluria.region.command.parser.type.RegionStringParser;
import io.github.aluria.region.entity.RegionObject;
import lombok.NonNull;

public final class RegionDisplayParser extends RegionStringParser {

    public RegionDisplayParser() {
        super("displayName");
    }

    @Override
    public void processProperty(@NonNull RegionObject regionObject, @NonNull String value) {
        regionObject.setDisplayName(value);
    }
}
