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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class PkEvent implements Listener {

    private final Spirits plugin = Spirits.plugin;

    private boolean hasSelectedSub = false;

    @EventHandler
    public void elementChange(PlayerChangeElementEvent event) {
        boolean guideEnabled = Spirits.plugin.getConfig().getBoolean("Command.ChooseElementGuide");
        boolean addSpiritElement = Spirits.plugin.getConfig().getBoolean("Command.GiveSpiritElementAutomatically");

        if (!guideEnabled)
            return;

        Player player = event.getTarget();
        Element element = event.getElement(),
                spirit = Element.getElement("Spirit"),
                lightSpirit = Element.getElement("LightSpirit"),
                darkSpirit = Element.getElement("DarkSpirit");

        if (player == null || element == null || !addSpiritElement) {
            return;
        }

        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (element.equals(spirit)) {
            new ChooseSub(player);
        } else if (element.equals(lightSpirit) || element.equals(darkSpirit)) {
            if (element.equals(lightSpirit)) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, -1);
            }
            if (!bPlayer.hasElement(Element.getElement("Spirit"))) {
                bPlayer.addElement(Element.getElement("Spirit"));
                GeneralMethods.sendBrandingMessage(player, ChatColor.DARK_AQUA + "You are now a Spirit.");
            }
            GeneralMethods.saveElements(bPlayer);
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().equalsIgnoreCase(ChooseSub.guiName)) {
            return;
        } else if ((event.getCurrentItem() == null) ||
                (event.getCurrentItem().equals(new ItemStack(Material.AIR))) ||
                event.getCurrentItem().getItemMeta() == null ||
                event.getCurrentItem().getItemMeta().getDisplayName().isEmpty()) {
            event.setCancelled(true);
            return;
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("LightSpirit")) {
            event.setCancelled(true);
            this.onElementSelection(player, Element.getElement("LightSpirit"));
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("DarkSpirit")) {
            event.setCancelled(true);
            this.onElementSelection(player, Element.getElement("DarkSpirit"));
        } else {
            event.setCancelled(true);
            return;
        }
        player.closeInventory();

    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String invName = event.getView().getTitle();

        if (!invName.equalsIgnoreCase(ChooseSub.guiName)) return;
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (!hasSelectedSub && bPlayer.hasElement(Element.getElement("Spirit"))) {
            bPlayer.getElements().remove(Element.getElement("Spirit"));
            GeneralMethods.saveElements(bPlayer);
            GeneralMethods.sendBrandingMessage(player, ChatColor.RED + "You failed to select your SubElement.");
        }
    }

    private void onElementSelection(Player player, Element element) {
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        ChatColor color = null;

        if (element.equals(Element.getElement("LightSpirit"))) {
            color = ChatColor.AQUA;
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
        } else {
            color = ChatColor.BLUE;
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, -1);
        }

        try {
            bPlayer.addElement(element);
            GeneralMethods.saveElements(bPlayer);

            GeneralMethods.sendBrandingMessage(player, color + "You are now a " + element.toString());
            hasSelectedSub = true;
        } catch (Exception e) {
            hasSelectedSub = false;
            plugin.getLogger().info("Failed to set Spirit element for " + player.getName() + ".");
            e.printStackTrace();
        }
    }
}