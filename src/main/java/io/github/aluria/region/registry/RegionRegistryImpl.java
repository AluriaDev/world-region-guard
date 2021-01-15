package io.github.aluria.region.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.util.sql.reader.SQLReader;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public final class RegionRegistryImpl extends RegionRegistry {

    private final static Ordering<Object> REVERSE_ORDERING;
    private final static Ordering<Comparable<?>> NATURAL_ORDERING;

    static {
        REVERSE_ORDERING = Ordering.from(Collections.reverseOrder());
        NATURAL_ORDERING = Ordering.natural();
    }

    private final Multimap<World, RegionObject> registry;

    public RegionRegistryImpl(@NonNull SQLReader sqlReader) {
        super(sqlReader);
        this.registry = HashMultimap.create();
        loadAllIntoRegistry(registry);
    }

    @Override
    public void registerRegion(@NonNull World world, @NonNull RegionObject regionObject) {
        registry.put(world, regionObject);
        save(regionObject);
    }

    @Override
    public void removeRegion(@NonNull World world, @NonNull String name) {
        final RegionObject regionObject = getRegionByName(world, name);
        if (regionObject != null) {
            removeRegion(world, regionObject);
        }
    }

    @Override
    public void removeRegion(@NonNull World world, @NonNull RegionObject regionObject) {
        registry.remove(world, regionObject);
        delete(regionObject);
    }

    @Override
    public boolean hasRegion(@NonNull World world, @NonNull String name) {
        return getRegionByName(world, name) != null;
    }

    @Override
    public RegionObject getRegionByName(@NonNull World world, @NonNull String name) {
        for (RegionObject regionObject : registry.get(world)) {
            if (regionObject.equals(name)) {
                return regionObject;
            }
        }
        return null;
    }

    @Override
    public RegionObject getOneRegionOnLocation(@NonNull Location location) {
        for (RegionObject regionObject : registry.get(location.getWorld())) {
            if (regionObject.isLocationInside(location)) {
                return regionObject;
            }
        }
        return null;
    }

    private List<RegionObject> _allRegionsOnLocation(@NonNull Location location, Ordering ordering) {
        final Set<RegionObject> regionObjects = new HashSet<>();
        for (RegionObject regionObject : registry.get(location.getWorld())) {
            if (regionObject.isLocationInside(location)) {
                regionObjects.add(regionObject);
            }
        }

        if (ordering == null) return new LinkedList<>(regionObjects);
        return ordering.sortedCopy(regionObjects);
    }

    @Override
    public RegionObject getOneRegionOnLocation(@NonNull Player player) {
        return getOneRegionOnLocation(player.getLocation());
    }

    @Override
    public List<RegionObject> getAllRegionsOnLocationNonOrdering(@NonNull Player player) {
        return getAllRegionsOnLocationNonOrdering(player.getLocation());
    }

    @Override
    public List<RegionObject> getAllRegionsOnLocationNonOrdering(@NonNull Location location) {
        return _allRegionsOnLocation(location, null);
    }

    @Override
    public List<RegionObject> getAllRegionsOnLocationNaturally(@NonNull Player player) {
        return getAllRegionsOnLocationNaturally(player.getLocation());
    }

    @Override
    public List<RegionObject> getAllRegionsOnLocationNaturally(@NonNull Location location) {
        return _allRegionsOnLocation(location, NATURAL_ORDERING);
    }

    @Override
    public List<RegionObject> getAllRegionsOnLocation(@NonNull Player player) {
        return getAllRegionsOnLocation(player.getLocation());
    }

    @Override
    public List<RegionObject> getAllRegionsOnLocation(@NonNull Location location) {
        return _allRegionsOnLocation(location, REVERSE_ORDERING);
    }

    @Override
    public RegionObject getHighestRegionOnLocation(@NonNull Player player) {
        return getHighestRegionOnLocation(player.getLocation());
    }

    @Override
    public RegionObject getHighestRegionOnLocation(@NonNull Location location) {
        final List<RegionObject> allRegionsOnLocation = getAllRegionsOnLocation(location);
        if (allRegionsOnLocation.isEmpty()) return null;
        return allRegionsOnLocation.get(0);
    }

    @Override
    public List<RegionObject> getAllRegionsContainer(@NonNull World world) {
        return new LinkedList<>(registry.get(world));
    }

    @Override
    public void saveAll(@NonNull World world) {
        saveAll(getAllRegionsContainer(world));
    }
}
