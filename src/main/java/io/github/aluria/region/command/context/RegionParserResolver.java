package io.github.aluria.region.command.context;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import com.google.common.collect.ImmutableList;
import io.github.aluria.region.command.parser.RegionPropertyParser;
import io.github.aluria.region.command.property.RegionDisplayParser;
import io.github.aluria.region.command.property.RegionPriorityParser;
import lombok.Getter;
import lombok.NonNull;

public final class RegionParserResolver implements ContextResolver<RegionPropertyParser, BukkitCommandExecutionContext> {

    @Getter
    private static final ImmutableList<RegionPropertyParser> processors;

    static {
        processors = ImmutableList
          .<RegionPropertyParser>builder()
          .add(
            new RegionDisplayParser(),
            new RegionPriorityParser()
          ).build();
    }

    @Override
    public RegionPropertyParser getContext(BukkitCommandExecutionContext context) throws InvalidCommandArgument {
        final RegionPropertyParser processor = getProcessor(context.popFirstArg());
        if (processor == null) {
            throw new InvalidCommandArgument("Não foi possível encontrar uma propriedade com este identificador.");
        }
        return processor;
    }

    private RegionPropertyParser getProcessor(@NonNull String name) {
        for (RegionPropertyParser processor : processors) {
            if (processor.getIdentifier().equalsIgnoreCase(name)) {
                return processor;
            }
        }
        return null;
    }
}
