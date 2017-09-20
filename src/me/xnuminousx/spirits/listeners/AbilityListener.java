package me.xnuminousx.spirits.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;

import me.xnuminousx.spirits.ability.spirit.Dash;
import me.xnuminousx.spirits.ability.spirit.Possess;
import me.xnuminousx.spirits.ability.spirit.Soar;
import me.xnuminousx.spirits.ability.dark.Intoxicate;
import me.xnuminousx.spirits.ability.dark.Shackle;
import me.xnuminousx.spirits.ability.light.Alleviate;
import me.xnuminousx.spirits.ability.light.Sanctity;

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
}
