package me.xnuminousx.spirits.ability.spirit;

import java.util.Random;

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

	private long time;
	private long duration;
	private double speed;
	private Location location;
	private long soarCooldown;

	public Soar(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		time = System.currentTimeMillis();
		setFields();
		start();
	}

	private void setFields() {
		this.soarCooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Soar.Cooldown");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Soar.Duration");
		this.speed = ConfigManager.getConfig().getDouble("Abilities.Spirits.Neutral.Agility.Soar.Speed");
		this.location = player.getLocation();
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
			remove();
			return;
		}
		if (bPlayer.isOnCooldown("Soar")) {
			remove();
			return;
		} else {
			progressSoar();
		}
	}

	private void progressSoar() {
		if (player.isSneaking()) {
			if (System.currentTimeMillis() > time + duration) {
				bPlayer.addCooldown("Soar", soarCooldown);
				remove();
				return;
			} else {
				Vector vec = player.getLocation().getDirection().normalize().multiply(speed);
				player.setVelocity(vec);
				if (new Random().nextInt(4) == 0) {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.3F, 5F);
				}
				Methods.playSpiritParticles(bPlayer, player.getLocation(), 0.2F, 0.2f, 0.2F, 0, 10);
			}
		} else {
			bPlayer.addCooldown("Soar", soarCooldown);
			remove();
			return;
		}
	}

	@Override
	public long getCooldown() {

		return soarCooldown;
	}

	@Override
	public Location getLocation() {

		return null;
	}

	@Override
	public String getName() {

		return "Agility";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("spirit", "Mobility") +
				ConfigManager.languageConfig.get().getString("Abilities.Spirit.Agility.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.spiritChatColor("spirit") + ConfigManager.languageConfig.get().getString("Abilities.Spirit.Agility.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.spiritChatColor("spirit") + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.spiritChatColor("spirit") + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Agility.Enabled");
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

		return false;
	}

	@Override
	public void load() {
	}

	@Override
	public void stop() {
	}

}
