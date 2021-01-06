package io.github.aluria.region;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.RegisteredCommand;
import io.github.aluria.region.api.registry.RegionRegistry;
import io.github.aluria.region.command.RegionFactoryCommand;
import io.github.aluria.region.listener.TriggerPlayerMove;
import io.github.aluria.region.registry.RegionRegistryImpl;
import io.github.aluria.region.selector.SelectorContainerWorld;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;

@Getter
public final class RegionGuardPlugin extends JavaPlugin {

    private RegionRegistry regionRegistry;
    private ServicesManager servicesManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        final Server server = getServer();
        final PluginManager pluginManager = server.getPluginManager();
        this.servicesManager = server.getServicesManager();

//        final Configuration configuration = getConfig();
//        final JdbcProvider jdbcProvider = getProvider(configuration);

//        final MessageProvider messageProvider = new MessageProvider(configuration);
//        new SelectorContainerJob(this);
        pluginManager.registerEvents(new TriggerPlayerMove(), this);
        registerService(this.regionRegistry = new RegionRegistryImpl());
        registerCommands();
    }

    private void registerCommands() {
        final BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.getLocales().setDefaultLocale(new Locale("pt", "BR"));
        commandManager.registerDependency(RegionRegistry.class, regionRegistry);
        commandManager.registerDependency(SelectorContainerWorld.class, SelectorContainerWorld.get());
        commandManager.registerCommand(new RegionFactoryCommand());
        commandManager.setDefaultExceptionHandler(this::exceptionHandler);
    }

    private boolean exceptionHandler(BaseCommand command, RegisteredCommand<?> registeredCommand, CommandIssuer sender, List<String> args, Throwable t) {
        getLogger().warning("Ocorreu um erro ao executar o comando '" + command.getName() + "'");
        return false;
    }

    private void registerService(@NonNull Object object) {
        servicesManager.register(
          (Class<Object>) object.getClass(),
          object,
          this,
          ServicePriority.Normal
        );
    }

//    private JdbcProvider getProvider(@NonNull Configuration configuration) {
//        return MysqlProvider.from(
//          MysqlCredential.fromConfiguration(
//            configuration.getConfigurationSection("connection.mysql")
//          )
//        ).preOpen();
//    }
}
