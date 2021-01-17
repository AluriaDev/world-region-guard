package io.github.aluria.region.entity;

import io.github.aluria.common.utils.Cuboid;
import io.github.aluria.common.utils.LocationUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Data
@Accessors(chain = true)
public final class RegionObject implements Comparable<RegionObject> {

    private final World worldBase;
    private final String name;
    private final Location start;
    private final Location end;
    private final Cuboid cuboid;
    private String permission;
    private int priority = 1;

    RegionObject(@NonNull String name, @NonNull Location start, @NonNull Location end) {
        this.worldBase = start.getWorld();
        this.name = name;
        this.start = start;
        this.end = end;
        this.cuboid = new Cuboid(start, end);
    }

    public String getWorldBaseName() {
        return worldBase.getName();
    }

    public String getRawLocationStart() {
        return LocationUtil.serialize(start);
    }

    public String getRawLocationEnd() {
        return LocationUtil.serialize(end);
    }

    public boolean isLocationInside(@NonNull Location location) {
        return cuboid.contains(location);
    }

    public boolean isPlayerInside(@NonNull Player player) {
        return isLocationInside(player.getLocation());
    }

    public boolean equals(String regionName) {
        return name.equalsIgnoreCase(regionName);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || object.getClass() != getClass()) return false;
        final RegionObject other = (RegionObject) object;
        if (!other.getWorldBase().equals(worldBase)) return false;
        return equals(other.getName());
    }

    @Override
    public int compareTo(RegionObject other) {
        return Integer.compare(priority, other.getPriority());
    }
}
