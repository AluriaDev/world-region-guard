package io.github.aluria.region.command.completion;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions.AsyncCommandCompletionHandler;
import co.aikar.commands.InvalidCommandArgument;
import io.github.aluria.region.command.context.RegionPropertyContext;
import io.github.aluria.region.logic.RegionPropertyProcessor;

import java.util.Collection;
import java.util.stream.Collectors;

public final class RegionPropertyCompletion implements AsyncCommandCompletionHandler<BukkitCommandCompletionContext> {

    @Override
    public Collection<String> getCompletions(BukkitCommandCompletionContext context) throws InvalidCommandArgument {
        return RegionPropertyContext
          .getProcessors()
          .parallelStream()
          .map(RegionPropertyProcessor::getIdentifier)
          .collect(Collectors.toList());
    }
}
