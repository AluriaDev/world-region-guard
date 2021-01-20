package io.github.aluria.region.bus.test;

import io.github.aluria.region.bus.event.PlayerRegionChangeEvent;
import io.github.aluria.region.bus.event.PlayerRegionJoinEvent;
import io.github.aluria.region.bus.event.PlayerRegionLeaveEvent;
import lombok.NonNull;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerRegionTest implements Listener {

    @EventHandler(ignoreCancelled = true)
    private void onPlayerRegionLeave(PlayerRegionLeaveEvent event) {
        sendActionbar(event.getPlayer(), event.getRegion().getDisplayName() + "§r leave");
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerRegionChange(PlayerRegionChangeEvent event) {
        sendActionbar(event.getPlayer(), "%s §r-> %s", event.getFrom().getDisplayName(), event.getTo().getDisplayName());
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerRegionJoin(PlayerRegionJoinEvent event) {
        sendActionbar(event.getPlayer(), event.getRegion().getDisplayName() + "§r joined");
    }

    private void sendActionbar(@NonNull Player player, @NonNull String message, @NonNull Object... objects) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format(message, objects)));
    }
}
