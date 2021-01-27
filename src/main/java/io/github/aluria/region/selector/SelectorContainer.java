package io.github.aluria.region.selector;

import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class SelectorContainer {

    private final static Map<Player, PlayerSelector> SELECTORS;

    static {
        SELECTORS = new HashMap<>();
    }

    public static PlayerSelector from(@NonNull Player player) {
        return SELECTORS.computeIfAbsent(player, key -> new PlayerSelector());
    }
}
