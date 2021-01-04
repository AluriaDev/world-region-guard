package io.github.aluria.region.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.aluria.region.entity.RegionObject;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Getter
public final class RegionRegistry {

    private final Multimap<String, RegionObject> regionRegistry;

    public RegionRegistry() {
        this.regionRegistry = HashMultimap.create();
    }

    public void createRegion(@NonNull Player player, @NonNull RegionObject regionObject) {
        regionRegistry.put(player.getLocation().getWorld().getName(), regionObject);
    }

    public RegionObject getRegion(@NonNull Player player, @NonNull String name) {
        return getRegion(player.getLocation(), name);
    }

    public RegionObject getRegion(@NonNull Location location, @NonNull String name) {
        return getRegion(location.getWorld(), name);
    }

    public RegionObject getRegion(@NonNull World world, @NonNull String name) {
        return getRegion(world.getName(), name);
    }

    private RegionObject getRegion(@NonNull String worldName, @NonNull String name) {
        for (RegionObject regionObject : regionRegistry.get(worldName)) {
            if (regionObject.getName().equalsIgnoreCase(name)) {
                return regionObject;
            }
        }
        return null;
    }

    public void removeRegion(@NonNull Location location, @NonNull String name) {
        final String worldName = location.getWorld().getName();
        final RegionObject region = getRegion(worldName, name);
        if (region != null) {
            regionRegistry.remove(worldName, region);
        }
    }
}
