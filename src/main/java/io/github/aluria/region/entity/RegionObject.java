package io.github.aluria.region.entity;

import io.github.aluria.common.utils.Cuboid;
import io.github.aluria.common.utils.LocationUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

@Data
@Accessors(chain = true)
public final class RegionObject extends RegionFlagObject implements Comparable<RegionObject> {

    private final UUID id;
    private final World world;
    private final String name;
    private final Location start;
    private final Location end;
    private final Cuboid cuboid;

    private String displayName;
    private String permission;
    private int priority;

    RegionObject(UUID id, @NonNull String name, @NonNull Location start, @NonNull Location end) {
        this.id = id;
        this.world = start.getWorld();
        this.name = name;
        this.start = start;
        this.end = end;
        this.cuboid = new Cuboid(start, end);
        this.priority = 1;
    }

    public String getDisplayName() {
        if (displayName == null) return name;
        return displayName;
    }

    public RegionObject setDisplayName(String displayName) {
        if (displayName != null) {
            this.displayName = displayName.replaceAll("&", "§");
        }
        return this;
    }

    public String getWorldName() {
        return world.getName();
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

    public boolean equalsName(String regionName) {
        return name.equalsIgnoreCase(regionName);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || object.getClass() != getClass()) return false;
        final RegionObject other = (RegionObject) object;
//        return other.getWorld().equals(world)
//          && id.equals(other.getId());
        return Objects.equals(id, other.getId());
    }

    @Override
    public int compareTo(RegionObject other) {
        return Integer.compare(priority, other.getPriority());
    }
}
