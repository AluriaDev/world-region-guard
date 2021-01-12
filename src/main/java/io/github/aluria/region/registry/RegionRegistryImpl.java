package io.github.aluria.region.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.entity.serialization.RegionObjectDeserializer;
import io.github.aluria.region.entity.serialization.RegionObjectQuery;
import io.github.aluria.region.entity.serialization.RegionObjectSerializer;
import io.github.aluria.region.util.sql.reader.SQLReader;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public final class RegionRegistryImpl implements RegionRegistry {

    private final static Ordering<Object> REVERSE_ORDERING;
    private final static Ordering<Comparable<?>> NATURAL_ORDERING;

    static {
        REVERSE_ORDERING = Ordering.from(Collections.reverseOrder());
        NATURAL_ORDERING = Ordering.natural();
    }

    private final RegionObjectDeserializer regionObjectDeserializer;
    private final RegionObjectSerializer regionObjectSerializer;
    private final Multimap<World, RegionObject> registry;
    private final RegionObjectQuery regionObjectQuery;

    public RegionRegistryImpl(@NonNull SQLReader reader) {
        this.regionObjectDeserializer = new RegionObjectDeserializer(reader);
        this.regionObjectSerializer = new RegionObjectSerializer();
        this.regionObjectQuery = new RegionObjectQuery(reader);
        this.registry = HashMultimap.create();
        regionObjectDeserializer.loadAllIntoRegistry(registry);
    }

    @Override
    public void registerRegion(@NonNull World world, @NonNull RegionObject regionObject) {
        registry.put(world, regionObject);
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

        if (ordering == null) return new ArrayList<>(regionObjects);
        return ordering.sortedCopy(regionObjects);
    }

    @Override
    public void removeRegion(@NonNull World world, @NonNull String name) {
        final RegionObject region = getRegionByName(world, name);
        if (region != null) {
            registry.remove(world, region);
            regionObjectQuery.deleteRegionFromDatabase(region);
        }
    }

    @Override
    public boolean hasRegion(@NonNull World world, @NonNull String name) {
        return getRegionByName(world, name) != null;
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
    public Set<RegionObject> getAllRegionsContainer(@NonNull World world) {
        return (Set<RegionObject>) registry.get(world);
    }
}
