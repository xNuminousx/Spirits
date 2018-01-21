package me.xnuminousx.spirits.ability.spirit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class Soar extends SpiritAbility implements AddonAbility {

	private Location location;
	private long cooldown;
	private long time;
	private long duration;
	private double speed;

	public Soar(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		
		start();
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Soar.Cooldown");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Soar.Duration");
		this.speed = ConfigManager.getConfig().getDouble("Abilities.Spirits.Neutral.Soar.Speed");
		this.location = player.getLocation();
	}

	@Override
	public void progress() {
		
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
			remove();
			return;
		}
		if (player.isSneaking()) {
			fly();
		} else {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
	}
	
	public void fly() {
		if (System.currentTimeMillis() > time + duration) {
			bPlayer.addCooldown(this);
			remove();
			return;
		} else {
			Vector vec = player.getLocation().getDirection().normalize().multiply(speed);
			player.setVelocity(vec);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.3F, 5F);
			Methods.spiritParticles(bPlayer, player.getLocation(), 0.2F, 0.2f, 0.2F, 0, 10);
		}
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
		return "Soar";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("spirit", "Mobility") +
				ConfigManager.languageConfig.get().getString("Abilities.Spirit.Soar.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.spiritChatColor("spirit") + ConfigManager.languageConfig.get().getString("Abilities.Spirit.Soar.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.spiritChatColor("spirit") + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return Methods.spiritChatColor("spirit") + "1.0";
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Soar.Enabled");
	}

	@Override
	public boolean isExplosiveAbility() {
		return false;
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
		return true;
	}

	@Override
	public void load() {

	}

	@Override
	public void stop() {

	}

}