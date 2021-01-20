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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

@RequiredArgsConstructor
public final class TriggerPlayerMove implements Listener {

    private final RegionRegistry regionRegistry;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onPlayerMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (isStatelessPlayer(from, to)) return;
        final Player player = event.getPlayer();
        if (handleTrigger(player, from, to)) return;
        player.teleport(from, TeleportCause.UNKNOWN);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == TeleportCause.UNKNOWN) return;
        final Player player = event.getPlayer();
        if (handleTrigger(player, event.getFrom(), event.getTo())) return;
        event.setCancelled(true);
    }

    private boolean handleTrigger(@NonNull Player player, @NonNull Location from, @NonNull Location to) {
        final RegionEvent regionEvent = getConditionEvent(
          player,
          regionRegistry.getHighestRegionOnLocation(from),
          regionRegistry.getHighestRegionOnLocation(to)
        );

        if (regionEvent == null) return true;
        regionEvent.perform();

        return !regionEvent.isCancelled();
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
