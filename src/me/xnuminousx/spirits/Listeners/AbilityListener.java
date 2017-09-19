package me.xnuminousx.spirits.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;

import me.xnuminousx.spirits.Abilities.Dark.Intoxicate;
import me.xnuminousx.spirits.Abilities.Dark.Shackle;
import me.xnuminousx.spirits.Abilities.Light.Alleviate;
import me.xnuminousx.spirits.Abilities.Spirit.Dash;
import me.xnuminousx.spirits.Abilities.Spirit.Possess;
import me.xnuminousx.spirits.Abilities.Spirit.Soar;

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
			
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Intoxicate")) {
			new Intoxicate(player);
			
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Alleviate")) {
			new Alleviate(player);
			
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Soar")) {
			new Soar(player);
		}
	}
}
