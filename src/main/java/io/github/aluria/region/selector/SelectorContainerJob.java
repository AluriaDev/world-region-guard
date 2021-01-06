package io.github.aluria.region.selector;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class SelectorContainerJob extends BukkitRunnable {

    public SelectorContainerJob(@NonNull Plugin plugin) {
        runTaskTimerAsynchronously(plugin, 0, 20);
    }

    @Override
    public void run() {
        final SelectorContainerWorld selectorContainerWorld = SelectorContainerWorld.get();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!selectorContainerWorld.hasEntry(player)) continue;

            final Location locationOne = selectorContainerWorld.getFirstLocation(player);
            final Location locationTwo = selectorContainerWorld.getSecondLocation(player);

            if (locationOne == null || locationTwo == null) continue;

            final World world = locationOne.getWorld();


        }
    }
}
