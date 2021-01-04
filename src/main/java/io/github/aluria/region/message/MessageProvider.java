package io.github.aluria.region.message;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public final class MessageProvider {

    private final Configuration configuration;
    private final ConfigurationSection commonSection;
    private final ConfigurationSection regionSection;

    public MessageProvider(@NonNull Configuration configuration) {
        this.configuration = configuration;
        commonSection = getSection("command.common");
        regionSection = getSection("command.region");
    }

    private ConfigurationSection getSection(@NonNull String key) {
        return configuration.getConfigurationSection(String.format("message_provider.%s", key));
    }

    public String format(@NonNull String text, Object... objects) {
        return String.format(text, objects).replace("&", "§");
    }

    public boolean sendCommon(@NonNull CommandSender sender, @NonNull String key, Object... objects) {
        sender.sendMessage(format(commonSection.getString(key), objects));
        return true;
    }

    public boolean sendRegion(@NonNull CommandSender sender, @NonNull String key, Object... objects) {
        sender.sendMessage(format(regionSection.getString(key), objects));
        return true;
    }
}
