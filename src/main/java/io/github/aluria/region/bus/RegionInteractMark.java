package io.github.aluria.region.bus;

import io.github.aluria.region.entity.RegionMarkStack;
import io.github.aluria.region.selector.SelectorContainerWorld;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public final class RegionInteractMark implements Listener {

    private final static SelectorContainerWorld CONTAINER_WORLD;

    static {
        CONTAINER_WORLD = SelectorContainerWorld.get();
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerInteract(PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (action != LEFT_CLICK_BLOCK && action != RIGHT_CLICK_BLOCK) return;
        if (!RegionMarkStack.isSimilar(event.getItem())) return;

        final Player player = event.getPlayer();
        if (!player.isOp()) return;

        final Location location = event.getClickedBlock().getLocation();
        final boolean result = action == LEFT_CLICK_BLOCK
          ? CONTAINER_WORLD.markFirstPosition(player, location)
          : CONTAINER_WORLD.markSecondPosition(player, location);

        if (!result) {
            player.sendMessage("§cNão foi possível definir o ponto de uma região, utilize §7'/region mark' §cpara saber mais.");
            return;
        }

        player.sendMessage(String.format("§aO ponto de uma região foi marcada em §7'%s'§a.", rawInfoLocation(location)));
        event.setCancelled(true);
    }

    private String rawInfoLocation(@NonNull Location location) {
        return String.format(
          "x: %s, y: %s, z: %s",
          location.getBlockX(),
          location.getBlockY(),
          location.getBlockZ()
        );
    }
}
