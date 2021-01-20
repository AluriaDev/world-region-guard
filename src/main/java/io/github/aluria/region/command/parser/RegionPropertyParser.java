package io.github.aluria.region.command.parser;

import co.aikar.commands.InvalidCommandArgument;
import io.github.aluria.region.entity.RegionObject;
import lombok.Getter;
import lombok.NonNull;

/**
 * Process property for a typed
 */
@Getter
public abstract class RegionPropertyParser<T> {

    private final String identifier;

    public RegionPropertyParser(@NonNull String identifier) {
        this.identifier = identifier;
    }

    public abstract T processRawProperty(@NonNull RegionObject regionObject, @NonNull String value) throws InvalidCommandArgument;

    public abstract void processProperty(@NonNull RegionObject regionObject, @NonNull T value);
}
