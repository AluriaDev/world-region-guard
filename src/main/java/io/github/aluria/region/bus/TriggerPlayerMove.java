package io.github.aluria.region.bus;

import io.github.aluria.region.bus.event.PlayerRegionChangeEvent;
import io.github.aluria.region.bus.event.PlayerRegionJoinEvent;
import io.github.aluria.region.bus.event.PlayerRegionLeaveEvent;
import io.github.aluria.region.bus.event.RegionEvent;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.registry.RegionRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@RequiredArgsConstructor
public final class TriggerPlayerMove implements Listener {

    private final RegionRegistry regionRegistry;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onPlayerMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (isStatelessPlayer(from, to)) return;

        final RegionObject fromRegion = regionRegistry.getHighestRegionOnLocation(from);
        final RegionObject toRegion = regionRegistry.getHighestRegionOnLocation(to);

        final Player player = event.getPlayer();
        final RegionEvent regionEvent = getConditionEvent(player, fromRegion, toRegion);
        if (regionEvent == null) return;

        regionEvent.perform();
        if (!regionEvent.isCancelled()) return;

        player.sendMessage("Cant into area");
        player.teleport(from);
    }

    private RegionEvent getConditionEvent(@NonNull Player player, RegionObject fromRegion, RegionObject toRegion) {
        if (fromRegion == toRegion) return null;
        if (toRegion == null) {
            return new PlayerRegionLeaveEvent(player, fromRegion);
        }
        if (fromRegion == null) {
            return new PlayerRegionJoinEvent(player, toRegion);
        }
        return new PlayerRegionChangeEvent(player, fromRegion, toRegion);
    }

    private boolean isStatelessPlayer(@NonNull Location from, @NonNull Location to) {
        return from.getBlockX() == to.getBlockX()
          && from.getBlockY() == to.getBlockY()
          && from.getBlockZ() == to.getBlockZ();
    }
}
