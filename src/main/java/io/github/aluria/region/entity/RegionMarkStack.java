package io.github.aluria.region.entity;

import io.github.aluria.common.utils.ItemBuilder;
import io.github.aluria.region.util.NBTCompoundUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public final class RegionMarkStack extends ItemBuilder {

    private static final ItemStack STACK_REFERENCE;

    static {
        STACK_REFERENCE = new RegionMarkStack().build();
    }

    public RegionMarkStack() {
        super(Material.STICK);
        name("§cAluria Region - Marker");
        lore(getLore());
        enchantment(Enchantment.DURABILITY, 1);
        flag(ItemFlag.HIDE_ENCHANTS);
        compound(nbtTagCompound -> {
            nbtTagCompound.setByte("aluria.region.mark", (byte) 1);
        });
    }

    public static ItemStack getStackReference() {
        return STACK_REFERENCE;
    }

    public static boolean isSimilar(ItemStack item) {
        return NBTCompoundUtil.hasCompoundEntryTag(item, "aluria.region.mark");
    }

    private String[] getLore() {
        return new String[]{
          "",
          " §7Item used to mark custom regions ",
          " §7Use §c§lLEFT CLICK §7to define first position",
          " §7and then use §c§lRIGHT CLICK §7to define the second position.",
          ""
        };
    }
}
