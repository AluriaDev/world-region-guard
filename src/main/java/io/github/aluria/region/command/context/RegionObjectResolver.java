package io.github.aluria.region.command.context;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.registry.RegionRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RegionObjectResolver implements ContextResolver<RegionObject, BukkitCommandExecutionContext> {

    private final RegionRegistry regionRegistry;

    @Override
    public RegionObject getContext(BukkitCommandExecutionContext context) throws InvalidCommandArgument {
        final RegionObject regionObject = regionRegistry.getRegionByName(
          context.getPlayer().getWorld(),
          context.popFirstArg()
        );

        if (regionObject == null) {
            throw new InvalidCommandArgument("Não foi possível encontrar uma região com este nome.");
        }

        return regionObject;
    }
}
