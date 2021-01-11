package io.github.aluria.region.bus.event;

import io.github.aluria.region.entity.RegionObject;
import org.bukkit.entity.Player;

public final class PlayerRegionJoinEvent extends RegionEvent {

    public PlayerRegionJoinEvent(Player who, RegionObject to) {
        this(who, to, RegionType.NORMAL);
    }

    public PlayerRegionJoinEvent(Player who, RegionObject to, RegionType type) {
        super(who, null, to, type);
    }

    public RegionObject getRegion() {
        return getTo();
    }
}
