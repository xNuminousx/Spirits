package me.numin.spirits.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent;
import me.numin.spirits.Spirits;
import me.numin.spirits.inventories.ChooseSub;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class PkEvent implements Listener {

    private boolean hasSelectedSub = false;

    // Used to automatically handle Spirit sub elements
    @EventHandler
    public void elementChange(PlayerChangeElementEvent event) {
        Player player = event.getTarget();
        Element element = event.getElement(), lightSpirit = Element.getElement("LightSpirit"), darkSpirit = Element.getElement("DarkSpirit");
        boolean addSpiritElement = Spirits.plugin.getConfig().getBoolean("Command.GiveSpiritElementAutomatically");

        if (player == null || element == null || !addSpiritElement) {
            return;
        }

        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (element.equals(Element.getElement("Spirit"))) {
            new ChooseSub(player);
        } else if (element.equals(lightSpirit) || element.equals(darkSpirit)) {
            if (element.equals(lightSpirit)) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, -1);
            }
            bPlayer.addElement(Element.getElement("Spirit"));
            GeneralMethods.saveElements(bPlayer);
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!event.getInventory().getTitle().contains(ChooseSub.guiName)) {
            return;
        } else if ((event.getCurrentItem() == null) ||
                (event.getCurrentItem().equals(new ItemStack(Material.AIR))) ||
                event.getCurrentItem().getItemMeta() == null ||
                event.getCurrentItem().getItemMeta().getDisplayName().isEmpty()) {
            event.setCancelled(true);
            return;
        }
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("LightSpirit")) {
            event.setCancelled(true);
            bPlayer.addElement(Element.getElement("LightSpirit"));
            GeneralMethods.saveElements(bPlayer);
            GeneralMethods.sendBrandingMessage((CommandSender)player, ChatColor.AQUA + "You are now a LightSpirit.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
            hasSelectedSub = true;
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("DarkSpirit")) {
            event.setCancelled(true);
            bPlayer.addElement(Element.getElement("DarkSpirit"));
            GeneralMethods.saveElements(bPlayer);
            GeneralMethods.sendBrandingMessage((CommandSender)player, ChatColor.BLUE + "You are now a DarkSpirit.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, -1);
            hasSelectedSub = true;
        } else {
            event.setCancelled(true);
            return;
        }
        player.closeInventory();

    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String invName = event.getInventory().getTitle();

        if (!invName.contains(ChooseSub.guiName)) {
            return;
        }
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (!hasSelectedSub && bPlayer.hasElement(Element.getElement("Spirit"))) {
            bPlayer.getElements().remove(Element.getElement("Spirit"));
            GeneralMethods.saveElements(bPlayer);
            GeneralMethods.sendBrandingMessage((CommandSender)player, ChatColor.RED + "You failed to select your SubElement.");
        }
    }
}