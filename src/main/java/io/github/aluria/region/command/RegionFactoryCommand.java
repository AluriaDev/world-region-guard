package io.github.aluria.region.command;

import io.github.aluria.region.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class RegionFactoryCommand implements CommandExecutor {

    private final MessageProvider provider;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return provider.sendCommon(sender, "only_players_can_execute");
        }

        final Player player = (Player) sender;

        return true;
    }
}
