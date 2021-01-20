package io.github.aluria.region.command.context;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandCompletions.AsyncCommandCompletionHandler;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import com.google.common.collect.ImmutableList;
import io.github.aluria.region.logic.RegionPropertyProcessor;
import io.github.aluria.region.logic.properties.RegionDisplayProcessor;
import io.github.aluria.region.logic.properties.RegionPriorityProcessor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.stream.Collectors;

public final class RegionPropertyContext implements ContextResolver<RegionPropertyProcessor, BukkitCommandExecutionContext> {

    @Getter
    private static final ImmutableList<RegionPropertyProcessor> processors;

    static {
        processors = ImmutableList
          .<RegionPropertyProcessor>builder()
          .add(
            new RegionDisplayProcessor(),
            new RegionPriorityProcessor()
          ).build();
    }

    @Override
    public RegionPropertyProcessor getContext(BukkitCommandExecutionContext context) throws InvalidCommandArgument {
        final RegionPropertyProcessor processor = getProcessor(context.popFirstArg());
        if (processor == null) {
            throw new InvalidCommandArgument("Não foi possível encontrar uma propriedade com este identificador.");
        }
        return processor;
    }

    private RegionPropertyProcessor getProcessor(@NonNull String name) {
        for (RegionPropertyProcessor processor : processors) {
            if (processor.getIdentifier().equalsIgnoreCase(name)) {
                return processor;
            }
        }
        return null;
    }
}
