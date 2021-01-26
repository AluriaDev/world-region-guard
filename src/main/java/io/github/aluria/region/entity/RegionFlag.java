package io.github.aluria.region.entity;

import io.github.aluria.region.command.parser.PropertyParser;
import io.github.aluria.region.entity.context.PlayerVision;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true, fluent = true)
public class RegionFlag {

    @PropertyParser
    private boolean canBreak = true;
    @PropertyParser
    private boolean canPlace = true;
    @PropertyParser
    private boolean canExecuteCommand = true;
    @PropertyParser
    private boolean canChat = true;
    @PropertyParser
    private PlayerVision playerVision;

    @PropertyParser
    private Set<String> whitelistCommands;

    public RegionFlag() {
        this.whitelistCommands = new HashSet<>();
    }
}
