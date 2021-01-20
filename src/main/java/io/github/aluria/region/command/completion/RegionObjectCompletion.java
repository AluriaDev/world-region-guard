package io.github.aluria.region.command.completion;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions.AsyncCommandCompletionHandler;
import co.aikar.commands.InvalidCommandArgument;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.registry.RegionRegistry;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class RegionObjectCompletion implements AsyncCommandCompletionHandler<BukkitCommandCompletionContext> {

    private final RegionRegistry regionRegistry;

    @Override
    public Collection<String> getCompletions(BukkitCommandCompletionContext context) throws InvalidCommandArgument {
        return regionRegistry
          .getAllRegionsContainer(context.getPlayer().getWorld())
          .parallelStream()
          .map(RegionObject::getName)
          .collect(Collectors.toList());
    }
}
