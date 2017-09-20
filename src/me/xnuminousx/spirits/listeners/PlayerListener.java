package me.xnuminousx.spirits.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent;
import com.projectkorra.projectkorra.event.PlayerChangeSubElementEvent;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.ability.spirit.Possess;
import me.xnuminousx.spirits.elements.SpiritElement;
import me.xnuminousx.spirits.misc.PlayerData;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerData.createPlayerData(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		if (bPlayer == null) {
			return;
		}
		if (CoreAbility.hasAbility(player, Possess.class)) {
			Possess possess = CoreAbility.getAbility(player, Possess.class);
			possess.remove();
		}
	}

	@EventHandler
	public void onElementChange(PlayerChangeElementEvent event) {
		if (event.getElement() == null) {
			return;
		}

		if (event.getResult().equals(PlayerChangeElementEvent.Result.CHOOSE) || event.getResult().equals(PlayerChangeElementEvent.Result.ADD)) {
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getTarget());
			PlayerData playerData = PlayerData.getPlayerData(event.getTarget());
			if (bPlayer == null) {
				return;
			} else if (playerData == null) {
				return;
			}
			if (!bPlayer.hasElement(SpiritElement.SPIRIT)) {
				return;
			}
			if (bPlayer.hasSubElementPermission(SpiritElement.DARK_SPIRIT) && bPlayer.hasSubElementPermission(SpiritElement.DARK_SPIRIT)) {
				SubElement spiritType = Math.random() > 0.5 ? SpiritElement.LIGHT_SPIRIT : SpiritElement.DARK_SPIRIT;
				Main.plugin.getLogger().warning("Player " + event.getTarget().getName() + " has permission for dark and light spirit. Check your permissions configuration to remove their access for 1 of these subelements.");
				Main.plugin.getLogger().info("Default falling back to " + spiritType.getName() + " for " + event.getTarget().getName());

				bPlayer.getSubElements().remove(SpiritElement.DARK_SPIRIT);
				bPlayer.getSubElements().remove(SpiritElement.LIGHT_SPIRIT);
				bPlayer.addSubElement(spiritType);
			}
			if (bPlayer.getSubElements().contains(SpiritElement.LIGHT_SPIRIT)) {
				playerData.setLightSpirit();
			} else if (bPlayer.getSubElements().contains(SpiritElement.DARK_SPIRIT)) {
				playerData.setDarkSpirit();
			}
		}
	}

	@EventHandler
	public void onSubElementChange(PlayerChangeSubElementEvent event) {
		if (event.getSubElement() == null) {
			return;
		}
		if (event.getResult().equals(PlayerChangeSubElementEvent.Result.CHOOSE) || event.getResult().equals(PlayerChangeSubElementEvent.Result.ADD)) {
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getTarget());
			PlayerData playerData = PlayerData.getPlayerData(event.getTarget());
			if (bPlayer == null) {
				return;
			} else if (playerData == null) {
				return;
			} else if (!bPlayer.hasElement(SpiritElement.SPIRIT)) {
				return;
			} else if (!bPlayer.hasSubElement(SpiritElement.DARK_SPIRIT) && !bPlayer.hasSubElement(SpiritElement.LIGHT_SPIRIT)) {
				return;
			}

			bPlayer.getSubElements().remove(SpiritElement.DARK_SPIRIT);
			bPlayer.getSubElements().remove(SpiritElement.LIGHT_SPIRIT);
			bPlayer.addSubElement(playerData.getSpiritType());
			GeneralMethods.sendBrandingMessage(event.getSender(), ChatColor.RED + "You are already a " + playerData.getSpiritType().getName() + " Spirit.");

			/*
			 * NOTE, currently in ProjectKorra there is no way to cancel the
			 * PlayerChangeElementEvent or PlayerChangeSubElementEvent, meaning
			 * we have to remove their subelements and re-add the ones they
			 * possess. Because of this, the player still gets the message
			 * saying they've added the new subelement, and then our error
			 * message saying they already are a spirit.
			 * 
			 * To fix this, we need to make these events implement Cancellable
			 * in ProjectKorra Core.
			 */

		}
	}

}