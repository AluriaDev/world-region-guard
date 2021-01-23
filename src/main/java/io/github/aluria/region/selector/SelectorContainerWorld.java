package io.github.aluria.region.selector;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

@Getter
public final class SelectorContainerWorld {

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final static SelectorContainerWorld get = new SelectorContainerWorld();

    private final Table<Player, Integer, Location> sections;

    public SelectorContainerWorld() {
        sections = HashBasedTable.create(2, 1);
    }

    public boolean markFirstPosition(@NonNull Player player, @NonNull Location location) {
        final Location secondLocation = sections.get(player, 1);
        if (secondLocation != null && notEquals(location, secondLocation)) {
            sections.remove(player, 1);
        }

        sections.put(player, 0, location);
        return true;
    }

    public boolean markSecondPosition(@NonNull Player player, @NonNull Location location) {
        final Location firstLocation = sections.get(player, 0);
        if (firstLocation != null && notEquals(location, firstLocation)) {
            return false;
        }

        sections.put(player, 1, location);
        return true;
    }

    public Location getLocation(@NonNull Player player, int index) {
        return sections.get(player, index);
    }

    public Location getSecondLocation(@NonNull Player player) {
        return getLocation(player, 1);
    }

    public Location getFirstLocation(@NonNull Player player) {
        return getLocation(player, 0);
    }

    public boolean hasEntry(@NonNull Player player) {
        return sections.containsRow(player);
    }

    private boolean notEquals(@NonNull Location positionOne, @NonNull Location positionTwo) {
        return !Objects.equals(positionOne.getWorld(), positionTwo.getWorld());
    }
}
