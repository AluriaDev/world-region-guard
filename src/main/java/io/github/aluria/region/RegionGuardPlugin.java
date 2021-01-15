package io.github.aluria.region;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.RegisteredCommand;
import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.wrapper.mysql.MysqlProvider;
import dev.king.universal.wrapper.mysql.api.MysqlCredential;
import io.github.aluria.region.bus.RegionInteractMark;
import io.github.aluria.region.bus.TriggerPlayerMove;
import io.github.aluria.region.bus.test.PlayerRegionTest;
import io.github.aluria.region.command.RegionFactoryCommand;
import io.github.aluria.region.command.RegionObjectResolver;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.registry.RegionRegistry;
import io.github.aluria.region.registry.RegionRegistryImpl;
import io.github.aluria.region.selector.SelectorContainerWorld;
import io.github.aluria.region.util.sql.reader.SQLReader;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;

@Getter
public final class RegionGuardPlugin extends JavaPlugin {

    private ServicesManager servicesManager;
    private RegionRegistry regionRegistry;
    private PluginManager pluginManager;
    private SQLReader sqlReader;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.sqlReader = new SQLReader(this, getProvider());
        if (!sqlReader.openConnection()) {
            getLogger().severe("Can't connect to the database");
            return;
        }

        sqlReader.createAllTableSchemas("region");

        final Server server = getServer();
        this.pluginManager = server.getPluginManager();
        this.servicesManager = server.getServicesManager();
        this.regionRegistry = new RegionRegistryImpl(sqlReader);

//        final MessageProvider messageProvider = new MessageProvider(configuration);
//        new SelectorContainerJob(this);
        registerService(regionRegistry);
        registerListeners(
          new TriggerPlayerMove(regionRegistry),
          new RegionInteractMark(),
          new PlayerRegionTest()
        );
        registerCommands();
    }

    @Override
    public void onDisable() {
        servicesManager.unregisterAll(this);
        sqlReader.closeConnection();
    }

    private void registerCommands() {
        final BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.getLocales().setDefaultLocale(new Locale("pt", "BR"));
        commandManager
          .getCommandContexts()
          .registerContext(RegionObject.class, new RegionObjectResolver(regionRegistry));

        commandManager.registerDependency(RegionRegistry.class, regionRegistry);
        commandManager.registerDependency(SelectorContainerWorld.class, SelectorContainerWorld.get());
        commandManager.registerCommand(new RegionFactoryCommand());
        commandManager.setDefaultExceptionHandler(this::exceptionHandler);
    }

    private void registerListeners(@NonNull Listener... listeners) {
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
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

    private MysqlProvider getProvider() {
        return (MysqlProvider) MysqlProvider.from(
          MysqlCredential.fromConfiguration(
            getConfig().getConfigurationSection("connection.mysql")
          )
        );
    }
}
