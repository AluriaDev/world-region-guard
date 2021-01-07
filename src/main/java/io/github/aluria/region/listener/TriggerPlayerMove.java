package io.github.aluria.region.listener;

import io.github.aluria.region.api.registry.RegionRegistry;
import io.github.aluria.region.entity.RegionObject;
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

        final Player player = event.getPlayer();

        final RegionObject fromRegion = regionRegistry.getHighestRegionOnLocation(from);
        final RegionObject toRegion = regionRegistry.getHighestRegionOnLocation(to);
        if (fromRegion == null && toRegion == null) return;

        if (fromRegion == null) {
            player.sendMessage("entrou");
            return;
        }

        if (toRegion == null) {
            player.sendMessage("saiu");
            return;
        }

        if (fromRegion != toRegion) {
            player.sendMessage("saiu");
            player.sendMessage("mudou de regiao");
            player.sendMessage("entrou");
        }
    }

    private boolean isStatelessPlayer(@NonNull Location from, @NonNull Location to) {
        return from.getBlockX() == to.getBlockX()
          && from.getBlockY() == to.getBlockY()
          && from.getBlockZ() == to.getBlockZ();
    }
}
