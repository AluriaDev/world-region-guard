package io.github.aluria.region.job;

import io.github.aluria.region.selector.SelectorContainerWorld;
import io.github.aluria.region.util.Cuboid;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
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

            final Location locationOne = selectorContainerWorld.getLocation(player, 0);
            final Location locationTwo = selectorContainerWorld.getLocation(player, 1);

            if (locationOne == null || locationTwo == null) continue;

            final World world = locationOne.getWorld();

            for (Block block : new Cuboid(locationOne, locationTwo)) {
                world.playEffect(block.getLocation(), Effect.HAPPY_VILLAGER, 1);
            }
        }
    }
}
