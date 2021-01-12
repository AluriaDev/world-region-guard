package io.github.aluria.region.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import java.util.Arrays;

@UtilityClass
public final class LocationUtil {

    public String serialize(@NonNull Location location) {
        return raw(
          String.valueOf(location.getBlockX()),
          String.valueOf(location.getBlockY()),
          String.valueOf(location.getBlockZ())
        );
    }

    public Location deserialize(@NonNull String world, @NonNull String rawLocation) {
        return from(Bukkit.getWorld(world), rawLocation.split(";"));
    }

    private Location from(@NonNull World world, String... locations) {
        return new Location(
          world,
          Integer.parseInt(locations[0]),
          Integer.parseInt(locations[1]),
          Integer.parseInt(locations[2])
        );
    }

    private String raw(String... strings) {
        return Strings.join(Arrays.asList(strings), ";");
    }
}
