package io.github.aluria.region.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import io.github.aluria.region.api.registry.RegionRegistry;
import io.github.aluria.region.entity.RegionObject;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public final class RegionRegistryImpl implements RegionRegistry {

    private final static Ordering<Object> REVERSE_ORDERING;
    private final static Ordering<Comparable<?>> NATURAL_ORDERING;

    static {
        REVERSE_ORDERING = Ordering.from(Collections.reverseOrder());
        NATURAL_ORDERING = Ordering.natural();
    }

    private final Multimap<World, RegionObject> registry;

    public RegionRegistryImpl() {
        this.registry = HashMultimap.create();
    }

    public void registerRegion(@NonNull World world, @NonNull RegionObject regionObject) {
        registry.put(world, regionObject);
    }

    public RegionObject getRegion(@NonNull World world, @NonNull String name) {
        for (RegionObject regionObject : registry.get(world)) {
            if (regionObject.equals(name)) {
                return regionObject;
            }
        }
        return null;
    }

    public void removeRegion(@NonNull World world, @NonNull String name) {
        final RegionObject region = getRegion(world, name);
        if (region != null) {
            registry.remove(world, region);
        }
    }

    public boolean hasRegion(@NonNull World world, @NonNull String name) {
        return getRegion(world, name) != null;
    }

    public RegionObject getPlayerRegion(@NonNull Player player) {
        final Location location = player.getLocation();
        for (RegionObject regionObject : registry.get(location.getWorld())) {
            if (regionObject.isLocationInside(location)) {
                return regionObject;
            }
        }
        return null;
    }

    public List<RegionObject> getPlayerRegionsNatural(@NonNull Player player) {
        return commonPlayerRegions(player, NATURAL_ORDERING);
    }

    public List<RegionObject> getPlayerRegions(@NonNull Player player) {
        return commonPlayerRegions(player, REVERSE_ORDERING);
    }

    private List<RegionObject> commonPlayerRegions(@NonNull Player player, @NonNull Ordering ordering) {
        final Location location = player.getLocation();
        final Set<RegionObject> regionObjects = new HashSet<>();
        for (RegionObject regionObject : registry.get(location.getWorld())) {
            if (regionObject.isLocationInside(location)) {
                regionObjects.add(regionObject);
            }
        }

        return ordering.sortedCopy(regionObjects);
    }

    public RegionObject getPlayerRegionHigh(@NonNull Player player) {
        return getPlayerRegions(player).get(0);
    }

    public Set<RegionObject> getRegions(@NonNull World world) {
        return (Set<RegionObject>) registry.get(world);
    }
}
