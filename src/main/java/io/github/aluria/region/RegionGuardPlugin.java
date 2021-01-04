package io.github.aluria.region;

import io.github.aluria.region.command.RegionFactoryCommand;
import io.github.aluria.region.job.SelectorContainerJob;
import io.github.aluria.region.listener.TriggerPlayerMove;
import io.github.aluria.region.message.MessageProvider;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public final class RegionGuardPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final Configuration configuration = getConfig();
//        final JdbcProvider jdbcProvider = getProvider(configuration);

        final MessageProvider messageProvider = new MessageProvider(configuration);
        new SelectorContainerJob(this);

        getServer().getPluginManager().registerEvents(new TriggerPlayerMove(), this);
        getCommand("region").setExecutor(new RegionFactoryCommand(messageProvider));
    }

//    private JdbcProvider getProvider(@NonNull Configuration configuration) {
//        return MysqlProvider.from(
//          MysqlCredential.fromConfiguration(
//            configuration.getConfigurationSection("connection.mysql")
//          )
//        ).preOpen();
//    }
}
