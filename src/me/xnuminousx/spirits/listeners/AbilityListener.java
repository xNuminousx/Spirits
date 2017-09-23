package me.xnuminousx.spirits.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;

import me.xnuminousx.spirits.ability.spirit.Dash;
import me.xnuminousx.spirits.ability.spirit.Possess;
import me.xnuminousx.spirits.ability.spirit.Soar;
import me.xnuminousx.spirits.ability.dark.Intoxicate;
import me.xnuminousx.spirits.ability.dark.Shackle;
import me.xnuminousx.spirits.ability.light.Alleviate;
import me.xnuminousx.spirits.ability.light.Sanctity;
import me.xnuminousx.spirits.ability.light.Shelter;

public class AbilityListener implements Listener {

	@EventHandler
	public void onSwing(PlayerAnimationEvent event) {

		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

		if (event.isCancelled() || bPlayer == null) {
			return;

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
			return;

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Dash")) {
			new Dash(player);

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Shackle")) {
			new Shackle(player);
			
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Shelter")) {
			new Shelter(player);
			
		}

	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {

		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

		if (event.isCancelled() || bPlayer == null) {
			return;

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
			return;

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Possess")) {
			new Possess(player);

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Alleviate")) {
			new Alleviate(player);

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Intoxicate")) {
			new Intoxicate(player);

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Sanctity")) {
			new Sanctity(player);

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Alleviate")) {
			new Alleviate(player);

		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Soar")) {
			new Soar(player);

		}

	}

	@EventHandler
	public void onPlayerFall(EntityDamageEvent event) {

		if (event.getEntity() instanceof Player) {
			Element element = Element.getElement("Spirit");

			Player player = (Player) event.getEntity();
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

			if (event.isCancelled() || bPlayer == null) {
				return;

			}

			if (bPlayer.hasElement(element) && event.getCause() == DamageCause.FALL) {
				event.setDamage(0D);
				event.setCancelled(true);
			}
		}

	}
}
