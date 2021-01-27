package io.github.aluria.region.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import io.github.aluria.region.command.parser.PropertyObject;
import io.github.aluria.region.entity.RegionMarkStack;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.entity.RegionValidator;
import io.github.aluria.region.registry.RegionRegistry;
import io.github.aluria.region.selector.PlayerSelector;
import io.github.aluria.region.selector.SelectorContainer;
import lombok.NonNull;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
@CommandAlias("região|region|regiao")
@CommandPermission("aluria.region.admin")
@Description("Aluria world region command")
public final class RegionFactoryCommand extends BaseCommand {

    private final static String[] HEADER_HELP_USAGE;

    static {
        HEADER_HELP_USAGE = new String[]{
          "",
          " §c§lALURIA §c- World Region Guard (Help)",
        };
    }

    @Dependency
    private RegionRegistry regionRegistry;

    @Default
    @HelpCommand
    @Syntax("[página]")
    public void onHelp(Player player, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("criar|create")
    @Syntax("<nome da região> [prioridade] [nome de exibição da região]")
    public void onCreate(Player player, @Single String regionName,
                         @Default("1") int priority,
                         @Optional String displayName) {
        final PlayerSelector playerSelector = SelectorContainer.from(player);
        final Location firstLocation = playerSelector.getStart();
        final Location secondLocation = playerSelector.getEnd();

        if (firstLocation == null || secondLocation == null) {
            player.sendMessage(helpUsage(
              " §cNão foi possível definir a região, por que: alguma das duas posições ainda não fora definidas. Algumas das causas são:",
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

        final RegionObject regionObject = RegionValidator
          .validate(world.getName(), regionName, firstLocation, secondLocation)
          .setPriority(priority)
          .setDisplayName(displayName);

        regionRegistry.registerRegion(world, regionObject);
        send(player, "§aA região §7'%s'§a foi criada com sucesso, com uma prioridade de §7'%s'§a.", regionName, priority);
    }

    @Subcommand("remover|remove")
    @Syntax("<nome da região>")
    @CommandCompletion("@region")
    public void onRemove(Player player, RegionObject regionObject) {
        regionRegistry.removeRegion(player.getWorld(), regionObject);
        send(player, "§aRegião §7'%s' §afoi removida dos registros deste mundo.", regionObject.getName());
    }

    @Subcommand("propriedade|property|prop")
    @CommandCompletion("@region @regionProcessor")
    @Syntax("<nome da região> <propriedade> <valor>")
    public void onProperty(Player player, RegionObject regionObject,
                           PropertyObject propertyObject,
                           String rawValue) {
        propertyObject.invokeMethod(regionObject, rawValue);
        /*final Object rawProperty = processor.processRawProperty(regionObject, rawValue);
        processor.processProperty(regionObject, rawProperty);*/
        regionRegistry.save(regionObject);

        send(
          player,
          "§aPropriedade §7'%s' §ada região §7'%s' §afoi definida para §7'%s'§a.",
          propertyObject.getIdentifier(),
          regionObject.getName(),
          rawValue
        );
    }

    @Subcommand("teleportar|teleport")
    @CommandCompletion("@region")
    @Syntax("<nome da região>")
    public void onTeleport(Player player, RegionObject regionObject) {
        player.teleport(regionObject.getCuboid().getCenter());
        send(player, "§aTeleportado para o centro da região §7'%s'§a.", regionObject.getName());
    }

    @Subcommand("demarcar|mark")
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
        player.getInventory().addItem(RegionMarkStack.getStackReference());
    }

    private String[] helpUsage(String... text) {
        return (String[]) ArrayUtils.addAll(HEADER_HELP_USAGE, text);
    }

    private void send(@NonNull Player player, @NonNull String text, Object... objects) {
        player.sendMessage(String.format(text, objects));
    }
}
