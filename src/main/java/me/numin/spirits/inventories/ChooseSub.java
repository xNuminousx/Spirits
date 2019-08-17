package me.numin.spirits.inventories;

import me.numin.spirits.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ChooseSub {

    public static String guiName = "Choose your SubElement";

    public ChooseSub(Player player) {
        int slotAmount = 9, lightSlot = 2, darkSlot = 6;
        Inventory inv = Bukkit.createInventory(player, slotAmount, guiName);

        List<String> light = new ArrayList<String>();
        light.add("LightSpirits specialize in aiding others with healing and defense.");
        light.add("They have a small amount of offense.");

        List<String> dark = new ArrayList<String>();
        dark.add("DarkSpirits specialize in disabling their opponents");
        dark.add("and dealing damage.");
        dark.add("They have little to no healing potential.");

        for (int i = 0; i <= slotAmount - 1; i++) {
            if (i != lightSlot && i != darkSlot) {
                if (i <= 3) {
                    inv.setItem(i, Methods.createItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "", ChatColor.AQUA));
                } else if (i > 4) {
                    inv.setItem(i, Methods.createItem(Material.RED_STAINED_GLASS_PANE, "", ChatColor.BLUE));
                } else {
                    inv.setItem(i, Methods.createItem(Material.NETHER_STAR, "Select your SubElement", ChatColor.GOLD));
                }
            }
        }

        inv.setItem(lightSlot, Methods.createItem(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, "LightSpirit", ChatColor.AQUA, light));
        inv.setItem(darkSlot, Methods.createItem(Material.BLACK_GLAZED_TERRACOTTA, "DarkSpirit", ChatColor.BLUE, dark));

        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.ENTITY_PIG_SADDLE, 1, (float) 0.7);
    }
}
