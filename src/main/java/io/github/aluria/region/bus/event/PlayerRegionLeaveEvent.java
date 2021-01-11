package io.github.aluria.region.bus.event;

import io.github.aluria.region.entity.RegionObject;
import org.bukkit.entity.Player;

public final class PlayerRegionLeaveEvent extends RegionEvent {

    public PlayerRegionLeaveEvent(Player who, RegionObject from) {
        this(who, from, RegionType.NORMAL);
    }

    public PlayerRegionLeaveEvent(Player who, RegionObject from, RegionType type) {
        super(who, from, null, type);
    }

    @Override
    public void perform() {
        makeInternal(this);
    }

    public RegionObject getRegion() {
        return getFrom();
    }
}
