package io.github.aluria.region.command.parser.type;

import io.github.aluria.region.command.parser.RegionPropertyParser;
import io.github.aluria.region.entity.RegionObject;
import lombok.NonNull;

public abstract class RegionStringParser extends RegionPropertyParser<String> {

    public RegionStringParser(@NonNull String identifier) {
        super(identifier);
    }

    @Override
    public String processRawProperty(@NonNull RegionObject regionObject, @NonNull String value) {
        return value;
    }
}
