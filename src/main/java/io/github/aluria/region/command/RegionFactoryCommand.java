package io.github.aluria.region.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.aluria.region.api.registry.RegionRegistry;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.selector.SelectorContainerWorld;
import lombok.NonNull;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandAlias("region|regiao|região")
@CommandPermission("aluria.region.admin")
@Description("Aluria world region command")
public final class RegionFactoryCommand extends BaseCommand {

    private final static String[] HEADER_HELP_USAGE = new String[]{
      "",
      " §c§lALURIA §c- World Region Guard (Help)",
    };

    @Dependency
    private SelectorContainerWorld containerWorld;
    @Dependency
    private RegionRegistry regionRegistry;

    @Subcommand("create|criar")
    @Syntax("<nome da região> [prioridade]")
    public void onCreate(Player player, @Single String regionName, @Default("1") int priority) {
        final Location firstLocation = containerWorld.getFirstLocation(player);
        final Location secondLocation = containerWorld.getSecondLocation(player);

        if (firstLocation == null || secondLocation == null) {
            player.sendMessage(helpUsage(
              " §cNão foi possível possivel a região, por quê: alguma das duas posições ainda não fora definidas. Algumas das causas são:",
              "  §r- §7Você ainda não as definiu.",
              "  §r- §7Você já as tinha definido, porém elas não eram do mesmo mundo e por isso o sistema de segurança cancelou a marcação. §8§o(Precaução para idiotas que forem fazer merda, assim como você está fazendo)",
              " ",
              " §e§oSe você ainda não sabe como demarcar uma região, utilize o comando §7'/região demarcar'§e§o.",
              ""
            ));
            return;
        }

        final World world = player.getWorld();
        if (regionRegistry.hasRegion(world, regionName)) {
            player.sendMessage("§cJá existe uma região com este nome.");
            return;
        }

        final RegionObject regionObject = createRegion(regionName, firstLocation, secondLocation, priority);
        regionRegistry.registerRegion(world, regionObject);
        player.sendMessage(String.format("§aA região §7'%s'§a foi criada com sucesso, com uma prioridade de §7'%s'§a.", regionName, priority));
    }

    @Subcommand("teste")
    public void onTeste(Player player) {
        final RegionObject playerRegion = regionRegistry.getPlayerRegion(player);
        final String regionName = playerRegion != null
          ? playerRegion.getName() : null;
        player.sendMessage("Is inside of any region? " + regionName);

        for (RegionObject region : regionRegistry.getPlayerRegions(player)) {
            player.sendMessage(String.format("%s with priority %s", region.getName(), region.getPriority()));
        }

        for (RegionObject region : regionRegistry.getRegions(player.getWorld())) {
            player.sendMessage(region.getName());
        }
    }

    @Subcommand("mark|demarcar")
    public void onMark(Player player) {
        player.sendMessage(helpUsage(
          " §a§oVocê está no menu de ajuda: onde aprenderá como demarcar regiões customizadas e protegidas para o funcionamento do servidor. Para podermos começar, primeiramente você precisa demarcar uma região, porém como se faz isso?",
          "",
          " §7§oEm seu inventário, você deve ter recebido um§e§o 'graveto'§7§o onde se é possível demarcar as regiões: ",
          "   §r- §cBotão esquerdo§e demarca o primeiro ponto da região.",
          "   §r- §cBotão direito§e demarca o segundo ponto da região.",
          "",
          " §r> §aDepois que ambas as posições estiverem definidas corretamente, você terá o resultado de um§c cubóide §8§o(paralelepípedo ortogonal de seis faces)§a.",
          "",
          " §c§lREGRAS:",
          "  §r- §7§oAs marcações precisam obrigatoriamente estar no mesmo mundo.",
          ""
        ));
    }

    private RegionObject createRegion(@NonNull String regionName, @NonNull Location firstLocation, @NonNull Location secondLocation, @NonNull int priority) {
        return new RegionObject(regionName, firstLocation, secondLocation)
          .setPriority(priority);
    }

    private String[] helpUsage(String... text) {
        return (String[]) ArrayUtils.addAll(HEADER_HELP_USAGE, text);
    }
}
