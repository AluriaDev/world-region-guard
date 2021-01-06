package io.github.aluria.region.api.registry;

import io.github.aluria.region.entity.RegionObject;
import lombok.NonNull;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public interface RegionRegistry {

    void registerRegion(@NonNull World world, @NonNull RegionObject regionObject);

    RegionObject getRegion(@NonNull World world, @NonNull String name);

    void removeRegion(@NonNull World world, @NonNull String name);

    boolean hasRegion(@NonNull World world, @NonNull String name);

    RegionObject getPlayerRegion(@NonNull Player player);

    List<RegionObject> getPlayerRegionsNatural(@NonNull Player player);

    List<RegionObject> getPlayerRegions(@NonNull Player player);

    RegionObject getPlayerRegionHigh(@NonNull Player player);

    Set<RegionObject> getRegions(@NonNull World world);
}
