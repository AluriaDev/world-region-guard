package io.github.aluria.region.entity;

import io.github.aluria.common.utils.Cuboid;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Data
@Accessors(chain = true)
public final class RegionObject implements Comparable<RegionObject> {

    private final String worldBaseName;
    private final String name;
    private final Location start;
    private final Location end;
    private final Cuboid cuboid;
    private String permission;
    private int priority = 1;

    public RegionObject(@NonNull String name, @NonNull Location start, @NonNull Location end) {
        this.worldBaseName = start.getWorld().getName();
        this.name = name;
        this.start = start;
        this.end = end;
        this.cuboid = new Cuboid(start, end);
    }

    public boolean equals(String regionName) {
        return name.equalsIgnoreCase(regionName);
    }

    public boolean isLocationInside(@NonNull Location location) {
        return cuboid.contains(location);
    }

    public boolean isPlayerInside(@NonNull Player player) {
        return isLocationInside(player.getLocation());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || object.getClass() != getClass()) return false;
        final RegionObject other = (RegionObject) object;
        if (!other.getWorldBaseName().equalsIgnoreCase(worldBaseName)) return false;
        return equals(other.getName());
    }

    @Override
    public int compareTo(RegionObject other) {
        return Integer.compare(priority, other.getPriority());
    }
}
