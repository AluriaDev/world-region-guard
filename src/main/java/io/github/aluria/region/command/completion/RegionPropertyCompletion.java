package io.github.aluria.region.command.completion;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions.AsyncCommandCompletionHandler;
import co.aikar.commands.InvalidCommandArgument;
import io.github.aluria.region.command.parser.PropertyFactory;
import io.github.aluria.region.command.parser.PropertyObject;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class RegionPropertyCompletion implements AsyncCommandCompletionHandler<BukkitCommandCompletionContext> {

    private final PropertyFactory propertyFactory;

    @Override
    public Collection<String> getCompletions(BukkitCommandCompletionContext context) throws InvalidCommandArgument {
        return propertyFactory
          .getPropertyObjects()
          .parallelStream()
          .map(PropertyObject::getIdentifier)
          .collect(Collectors.toSet());
    }
}
