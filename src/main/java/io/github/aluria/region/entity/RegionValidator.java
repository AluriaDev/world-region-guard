package io.github.aluria.region.entity;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;

@UtilityClass
public final class RegionValidator {

    public RegionObject validate(@NonNull String name, @NonNull Location start, Location end) throws IllegalStateException {
        if (name.length() > 25) {
            throw new IllegalStateException("Region name cannot be longer than 25 characters");
        }

        if (!start.getWorld().equals(end.getWorld())) {
            throw new IllegalStateException("The region's position must be at the same world");
        }

        return new RegionObject(name, start, end);
    }
}
