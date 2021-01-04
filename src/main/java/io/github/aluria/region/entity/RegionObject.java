package io.github.aluria.region.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@Data
@RequiredArgsConstructor
public final class RegionObject {

    private final String name;
    private final Location start;
    private final Location end;

    private int priority = 1;
    private String permission;
}
