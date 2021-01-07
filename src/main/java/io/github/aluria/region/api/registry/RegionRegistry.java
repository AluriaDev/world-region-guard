package io.github.aluria.region.api.registry;

import io.github.aluria.region.entity.RegionObject;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public interface RegionRegistry {

    /**
     * Adds a region to the registration of a world (All regions are separated
     * by containers that organize the project hierarchy)
     *
     * @param world        instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param regionObject region instance, created using the {@link RegionObject#RegionObject(String, Location, Location)} constructor of the class itself
     */
    void registerRegion(@NonNull World world, @NonNull RegionObject regionObject);

    /**
     * Get a region through her name, from the world container
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param name  name of the region you want to purchase
     * @return requested region instance
     */
    RegionObject getRegion(@NonNull World world, @NonNull String name);

    /**
     * Removes a region from the records, using its name
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param name  name of the region you want to delete
     */
    void removeRegion(@NonNull World world, @NonNull String name);

    /**
     * Check if a region already exists in the records
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @param name  name of the region you want to check whether or not it exists
     * @return true if region exists, otherwise false
     */
    boolean hasRegion(@NonNull World world, @NonNull String name);

    /**
     * Obtains the region where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return a random region (registration preference, which was first registered)
     */
    RegionObject getPlayerRegion(@NonNull Player player);

    /**
     * Obtains all regions where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return get all the regions that exist in your current location
     * but unlike the {@link RegionRegistry#getPlayerRegions(Player)} method it returns from the smallest to the largest, in its natural order
     */
    List<RegionObject> getPlayerRegionsNatural(@NonNull Player player);

    /**
     * Obtains all regions where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return get all the regions that exist in your current location
     * but unlike the {@link RegionRegistry#getPlayerRegionsNatural(Player)} method it returns from highest to lowest, in reverse order
     */
    List<RegionObject> getPlayerRegions(@NonNull Player player);

    /**
     * Obtains the region where the player is located, verified through its current location
     *
     * @param player instance of a valid {@link Player}
     * @return a region with higher priority, in reverse order. Ex: [spawn: priority = 10, lobby = priority = 20] will return the lobby for having a higher priority than spawn
     */
    RegionObject getPlayerRegionHigh(@NonNull Player player);

    /**
     * Get all container regions in the world
     *
     * @param world instance of a valid world, obtained through {@link org.bukkit.Bukkit#getWorld(String)}
     * @return collection with all unique regions (there will be no duplicate regions due to the {@link Set} interface
     */
    Set<RegionObject> getRegions(@NonNull World world);
}