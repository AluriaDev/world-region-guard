package io.github.aluria.region.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class RegionFlag {

    private boolean canBreak = true;
    private boolean canPlace = true;
    private boolean canExecuteCommand = true;
    private boolean canChat = true;

    private Set<String> whitelistCommands;

    public RegionFlag() {
        this.whitelistCommands = new HashSet<>();
    }
}
