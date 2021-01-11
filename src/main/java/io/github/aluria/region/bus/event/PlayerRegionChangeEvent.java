package io.github.aluria.region.bus.event;

import io.github.aluria.region.entity.RegionObject;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public final class PlayerRegionChangeEvent extends RegionEvent {

    private final PlayerRegionLeaveEvent leaveEvent;
    private final PlayerRegionJoinEvent joinEvent;

    public PlayerRegionChangeEvent(Player who, RegionObject from, RegionObject to) {
        super(who, from, to, RegionType.NORMAL);
        this.leaveEvent = new PlayerRegionLeaveEvent(who, from, RegionType.CHANGED_REGION);
        this.joinEvent = new PlayerRegionJoinEvent(who, to, RegionType.CHANGED_REGION);
    }

    @Override
    public void perform() {
        makeInternal(leaveEvent, this, joinEvent);
    }

    @Override
    public boolean isCancelled() {
        if (leaveEvent == null || joinEvent == null) return false;
        return leaveEvent.isCancelled() && joinEvent.isCancelled();
    }
}
