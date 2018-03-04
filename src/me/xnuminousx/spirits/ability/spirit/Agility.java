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

public class Agility extends SpiritAbility implements AddonAbility {

	public enum AgilityType {
		SOAR, DASH
	}
	private AgilityType agilityType;

	private long dashCooldown;
	private long soarCooldown;
	private Location location;
	
	//Dash
	private long distance;
	
	//Soar
	private long time;
	private long duration;
	private double speed;
	
	public Agility(Player player, AgilityType agilityType) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		this.agilityType = agilityType;
		time = System.currentTimeMillis();
		setFields();
		start();
	}

	private void setFields() {
		//Dash
		this.dashCooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Dash.Cooldown");
		this.distance = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Dash.Distance");
		
		//Soar
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
		if (this.agilityType == AgilityType.DASH) {
			progressDash();
		} else if (this.agilityType == AgilityType.SOAR) {
			progressSoar();
		}
	}
	
	public void progressDash() {
		Location loc = player.getLocation();
		Methods.setPlayerVelocity(player, loc, true, distance, 0.2);
		loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_HURT, 1.5F, 0.5F);
		loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.3F, 0.5F);
		Methods.spiritParticles(bPlayer, player.getLocation(), 0.2F, 0.2f, 0.2F, 0, 10);
		bPlayer.addCooldown(this, dashCooldown);
		remove();
		return;
	}
	
	public void progressSoar() {
		if (player.isSneaking()) {
			if (System.currentTimeMillis() > time + duration) {
				bPlayer.addCooldown(this, soarCooldown);
				remove();
				return;
			} else {
				Vector vec = player.getLocation().getDirection().normalize().multiply(speed);
				player.setVelocity(vec);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.3F, 5F);
				Methods.spiritParticles(bPlayer, player.getLocation(), 0.2F, 0.2f, 0.2F, 0, 10);
			}
		} else {
			bPlayer.addCooldown(this, soarCooldown);
			remove();
			return;
		}
	}

	@Override
	public long getCooldown() {

		return 0;
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
