package io.github.aluria.region.entity;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@UtilityClass
public final class RegionValidator {

    public RegionObject validate(@NonNull String worldName, @NonNull String name, @NonNull Location start, Location end) throws IllegalStateException {
        final UUID uuid = UUID.nameUUIDFromBytes(
          String.format("%s-%s", worldName, name)
            .getBytes(StandardCharsets.UTF_8)
        );
        return validate(uuid, name, start, end);
    }

    public RegionObject validate(@NonNull UUID uuid, @NonNull String name, @NonNull Location start, Location end) throws IllegalStateException {
        if (name.length() > 25) {
            throw new IllegalStateException("Region name cannot be longer than 25 characters");
        }

        if (!start.getWorld().equals(end.getWorld())) {
            throw new IllegalStateException("The region's position must be at the same world");
        }

        return new RegionObject(uuid, name, start, end);
    }
}
