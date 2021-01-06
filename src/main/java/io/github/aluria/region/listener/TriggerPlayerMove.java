package io.github.aluria.region.listener;

import io.github.aluria.region.selector.SelectorContainerWorld;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class TriggerPlayerMove implements Listener {

//    @EventHandler(ignoreCancelled = true)
//    private void onPlayerMove(PlayerMoveEvent event) {
//
//    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerInteract(PlayerInteractEvent event) {
        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        final Player player = event.getPlayer();
        final Location location = clickedBlock.getLocation();

        boolean result;
        final SelectorContainerWorld selectorContainerWorld = SelectorContainerWorld.get();
        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK:
                result = selectorContainerWorld.markFirstPosition(player, location);
                break;
            case RIGHT_CLICK_BLOCK:
                result = selectorContainerWorld.markSecondPosition(player, location);
                break;
            default:
                return;
        }

        if (!result) {
            player.sendMessage("Cant mark region");
            return;
        }
        player.sendMessage("Region marked");
    }
}
