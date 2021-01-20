package io.github.aluria.region.logic.processor;

import co.aikar.commands.InvalidCommandArgument;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.logic.RegionPropertyProcessor;
import lombok.NonNull;

public abstract class RegionIntegerProperty extends RegionPropertyProcessor<Integer> {

    public RegionIntegerProperty(@NonNull String identifier) {
        super(identifier);
    }

    @Override
    public Integer processRawProperty(@NonNull RegionObject regionObject, @NonNull String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new InvalidCommandArgument("Precisa ser um número.");
        }
    }
}
