package io.github.aluria.region.bus.test;

import io.github.aluria.region.bus.event.PlayerRegionChangeEvent;
import io.github.aluria.region.bus.event.PlayerRegionJoinEvent;
import io.github.aluria.region.bus.event.PlayerRegionLeaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerRegionTest implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerRegionLeave(PlayerRegionLeaveEvent event) {
        event.getPlayer().sendMessage(event.getRegion().getDisplayName() + " leave");
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerRegionChange(PlayerRegionChangeEvent event) {
        event.getPlayer().sendTitle(String.format("%s -> %s", event.getFrom().getDisplayName(), event.getTo().getDisplayName()),"");
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerRegionJoin(PlayerRegionJoinEvent event) {
        event.getPlayer().sendMessage(event.getRegion().getDisplayName() + " joined");
    }
}
