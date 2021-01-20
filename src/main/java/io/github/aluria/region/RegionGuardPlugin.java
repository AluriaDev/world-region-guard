package io.github.aluria.region;

import dev.king.universal.wrapper.mysql.MysqlProvider;
import dev.king.universal.wrapper.mysql.api.MysqlCredential;
import io.github.aluria.common.plugin.AluriaPlugin;
import io.github.aluria.region.bus.RegionInteractMark;
import io.github.aluria.region.bus.TriggerPlayerMove;
import io.github.aluria.region.bus.test.PlayerRegionTest;
import io.github.aluria.region.command.RegionFactoryCommand;
import io.github.aluria.region.command.completion.RegionObjectCompletion;
import io.github.aluria.region.command.completion.RegionPropertyCompletion;
import io.github.aluria.region.command.context.RegionObjectResolver;
import io.github.aluria.region.command.context.RegionParserResolver;
import io.github.aluria.region.command.parser.RegionPropertyParser;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.registry.RegionRegistry;
import io.github.aluria.region.registry.RegionRegistryImpl;
import io.github.aluria.region.selector.SelectorContainerWorld;
import io.github.aluria.region.util.sql_reader.SQLReader;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public final class RegionGuardPlugin extends AluriaPlugin {

    private RegionRegistry regionRegistry;
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

        this.regionRegistry = new RegionRegistryImpl(sqlReader);

//        final MessageProvider messageProvider = new MessageProvider(configuration);
//        new SelectorContainerJob(this);

        registerDependency(RegionRegistry.class, regionRegistry);
        registerDependencies(SelectorContainerWorld.get());
        registerContextResolver(RegionObject.class, new RegionObjectResolver(regionRegistry));
        registerContextResolver(RegionPropertyParser.class, new RegionParserResolver());
        registerAsyncCompletion("region", new RegionObjectCompletion(regionRegistry));
        registerAsyncCompletion("regionProcessor", new RegionPropertyCompletion());

        registerCommands(new RegionFactoryCommand());

        registerListeners(
          new TriggerPlayerMove(regionRegistry),
          new RegionInteractMark(),
          new PlayerRegionTest()
        );
    }

    @Override
    public void onDisable() {
        regionRegistry.saveAll();
        Bukkit.getServicesManager().unregisterAll(this);
        sqlReader.closeConnection();
    }

    private MysqlProvider getProvider() {
        return (MysqlProvider) MysqlProvider.from(
          MysqlCredential.fromConfiguration(
            getConfig().getConfigurationSection("connection.mysql")
          )
        );
    }
}
