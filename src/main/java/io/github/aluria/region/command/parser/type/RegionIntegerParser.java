package io.github.aluria.region.command.parser.type;

import co.aikar.commands.InvalidCommandArgument;
import io.github.aluria.region.command.parser.RegionPropertyParser;
import io.github.aluria.region.entity.RegionObject;
import lombok.NonNull;

public abstract class RegionIntegerParser extends RegionPropertyParser<Integer> {

    public RegionIntegerParser(@NonNull String identifier) {
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
