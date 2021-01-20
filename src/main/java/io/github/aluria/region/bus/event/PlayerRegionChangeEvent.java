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
    public synchronized void perform() {
        leaveEvent.perform();
        super.setCancelled(leaveEvent.isCancelled());
        super.perform();
        joinEvent.setCancelled(super.isCancelled());
        joinEvent.perform();
    }

    @Override
    public synchronized boolean isCancelled() {
        if (leaveEvent == null || joinEvent == null) return false;
        return leaveEvent.isCancelled()
          || super.isCancelled()
          || joinEvent.isCancelled();
    }
}
