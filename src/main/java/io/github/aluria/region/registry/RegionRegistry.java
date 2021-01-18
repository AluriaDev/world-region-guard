package io.github.aluria.region.registry;

import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.util.sql_reader.SQLReader;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class RegionRegistry extends RegionDAO {

    protected RegionRegistry(@NonNull SQLReader sqlReader) {
        super(sqlReader);
    }

    /**
     * Adds a region to the registration of a world (All regions are separated
     * by containers that organize the project hierarchy)
     *
     * @param world        instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param regionObject region instance, created using the {@link io.github.aluria.region.entity.RegionValidator#validate(String, Location, Location)}
     */
    public abstract void registerRegion(@NonNull World world, @NonNull RegionObject regionObject);

    /**
     * Get a region through her name, from the world container
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param name  name of the region you want to purchase
     * @return requested region instance
     */
    public abstract RegionObject getRegionByName(@NonNull World world, @NonNull String name);

    /**
     * Removes a region from the registries, using its name
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param name  name of the region you want to delete
     */
    public abstract void removeRegion(@NonNull World world, @NonNull String name);

    /**
     * Removes a region from the registries, using instance of object
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param regionObject region instance, created using the {@link io.github.aluria.region.entity.RegionValidator#validate(String, Location, Location)}
     */
    public abstract void removeRegion(@NonNull World world, @NonNull RegionObject regionObject);

    /**
     * Check if a region already exists in the registries
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param name  name of the region you want to check whether or not it exists
     * @return true if region exists, otherwise false
     */
    public abstract boolean hasRegion(@NonNull World world, @NonNull String name);

    /**
     * Obtains the region where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return the first occurrence within the AABB (Axis-Aligned Bounding Box)
     */
    public abstract RegionObject getOneRegionOnLocation(@NonNull Player player);

    /**
     * Obtains the region where the player is located, verified through its current location
     *
     * @param location instance of {@link Location}
     * @return the first occurrence within the AABB (Axis-Aligned Bounding Box)
     */
    public abstract RegionObject getOneRegionOnLocation(@NonNull Location location);

    /**
     * Obtains all regions where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return get all the regions that exist in your current location with non-ordering sort priority
     */
    public abstract List<RegionObject> getAllRegionsOnLocationNonOrdering(@NonNull Player player);

    /**
     * Obtains all regions where the player is located, verified through its current location
     *
     * @param location instance of {@link Location}
     * @return get all the regions that exist in your current location with non-ordering sort priority
     */
    public abstract List<RegionObject> getAllRegionsOnLocationNonOrdering(@NonNull Location location);

    /**
     * Obtains all regions where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return get all the regions that exist in your current location
     * but unlike the {@link RegionRegistry#getAllRegionsOnLocation(Player)} method it returns from the smallest to the largest, in its natural order
     */
    public abstract List<RegionObject> getAllRegionsOnLocationNaturally(@NonNull Player player);

    /**
     * Obtains all regions where the player is located, verified through its current location
     *
     * @param location instance of {@link Location}
     * @return get all the regions that exist in your current location
     * but unlike the {@link RegionRegistry#getAllRegionsOnLocation(Location)} method it returns from the smallest to the largest, in its natural order
     */
    public abstract List<RegionObject> getAllRegionsOnLocationNaturally(@NonNull Location location);

    /**
     * Obtains all regions where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return get all the regions that exist in your current location
     * but unlike the {@link RegionRegistry#getAllRegionsOnLocationNaturally(Player)} method it returns from highest to lowest, in reverse order
     */
    public abstract List<RegionObject> getAllRegionsOnLocation(@NonNull Player player);

    /**
     * Obtains all regions where the player is located, verified through its current location
     *
     * @param location instance of {@link Location}
     * @return get all the regions that exist in your current location
     * but unlike the {@link RegionRegistry#getAllRegionsOnLocationNaturally(Location)} method it returns from highest to lowest, in reverse order
     */
    public abstract List<RegionObject> getAllRegionsOnLocation(@NonNull Location location);

    /**
     * Obtains the region where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return a region with higher priority, in reverse order. Ex: [spawn: priority = 10, lobby = priority = 20] will return the lobby for having a higher priority than spawn
     */
    public abstract RegionObject getHighestRegionOnLocation(@NonNull Player player);

    /**
     * Obtains the region where the player is located, verified through its current location
     *
     * @param location instance of {@link Location}
     * @return a region with higher priority, in reverse order. Ex: [spawn: priority = 10, lobby = priority = 20] will return the lobby for having a higher priority than spawn
     */
    public abstract RegionObject getHighestRegionOnLocation(@NonNull Location location);

    /**
     * Get all container regions in the world
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @return collection with all unique regions (there will be no duplicate regions due to the {@link List} interface
     */
    public abstract List<RegionObject> getAllRegionsContainer(@NonNull World world);

    /**
     * Save all the container regions into to the database
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     */
    public abstract void saveAll(@NonNull World world);

    /**
     * Save all the worlds regions container into the database
     */
    public abstract void saveAll();
}
