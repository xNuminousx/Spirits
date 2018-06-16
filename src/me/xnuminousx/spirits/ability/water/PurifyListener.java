package com.Pride.korra.Purify;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class PurifyListener implements Listener {
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		if (!event.isSneaking()) {
			return;
		}
		
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());
		if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("Purify")) && CoreAbility.getAbility(event.getPlayer(), Purify.class) == null) {
			new Purify(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.isCancelled()) return;
		
		if (event.getEntity() instanceof Player) {
			BendingPlayer bPlayer = BendingPlayer.getBendingPlayer((Player) event.getEntity());
			if (bPlayer != null && CoreAbility.getAbility((Player) event.getEntity(), Purify.class) != null) {
				Purify ability = CoreAbility.getAbility((Player) event.getEntity(), Purify.class);
				ability.remove();
				ParticleEffect.CRIT.display(0.5F, 0.5F, 0.5F, 0.1F, 25, event.getEntity().getLocation().clone().add(0, 1.6, 0), 90);
			}
		}
		
		
	}

}
