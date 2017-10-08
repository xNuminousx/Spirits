package me.xnuminousx.spirits.ability.spirit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.SpiritAbility;
import net.md_5.bungee.api.ChatColor;

public class Dash extends SpiritAbility implements AddonAbility {
	
	private Location location;
	private long distance;
	private long cooldown;

	public Dash(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		start();
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Dash.Cooldown");
		this.distance = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Dash.Distance");
		this.location = player.getLocation();
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
			remove();
			return;
		}
		dash();
		bPlayer.addCooldown(this);
		remove();
		return;
	}
	
	public void dash() {
		Location loc = player.getLocation();
		Methods.setPlayerVelocity(player, loc, true, distance, 0.2);
		loc.getWorld().playSound(location, Sound.ENTITY_ELDER_GUARDIAN_HURT, 1.5F, 0.5F);
		loc.getWorld().playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.3F, 0.5F);
		Methods.spiritParticles(bPlayer, player.getLocation(), 0.2F, 0.2f, 0.2F, 0, 10);
		return;
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "Dash";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("spirit", "Mobility", "Sometimes, in intense battles, a Spirit may dart from one location to the next! Useful to escape, evade or just plain exploring.");
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.BLUE + "Left-Click";
	}

	@Override
	public String getAuthor() {
		return ChatColor.BLUE + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return ChatColor.BLUE + "1.0";
	}

	@Override
	public boolean isExplosiveAbility() {
		return false;
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Dash.Enable");
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public boolean isIgniteAbility() {
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void load() {

	}

	@Override
	public void stop() {

	}

}
