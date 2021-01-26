package io.github.aluria.region.command.context;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import io.github.aluria.region.command.parser.PropertyFactory;
import io.github.aluria.region.command.parser.PropertyObject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RegionParserResolver implements ContextResolver<PropertyObject, BukkitCommandExecutionContext> {

    private final PropertyFactory propertyFactory;

    @Override
    public PropertyObject getContext(BukkitCommandExecutionContext context) throws InvalidCommandArgument {
        final PropertyObject propertyObject = propertyFactory.getPropertyObject(context.popFirstArg());
        if (propertyObject == null) {
            throw new InvalidCommandArgument("Não foi possível encontrar uma propriedade com este identificador.");
        }
        return propertyObject;
    }
}
