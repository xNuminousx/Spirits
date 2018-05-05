package me.xnuminousx.spirits.ability.spirit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.Methods.SpiritType;
import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class Vanish extends SpiritAbility implements AddonAbility {

	private long cooldown;
	private long time;
	private long duration;
	private boolean playSound;
	private Location origin;
	private Location targetLoc;
	private long range;
	private long radius;

	public Vanish(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		setFields();
		time = System.currentTimeMillis();
		start();
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Vanish.Cooldown");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Vanish.Duration");
		this.range = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Vanish.Range");
		this.radius = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Vanish.Radius");
		this.origin = player.getLocation();
		this.playSound = true;

	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, player.getLocation())) {
			remove();
			return;
		}
		vanishPlayer();
		if (playSound) {
			playSound = false;
			playEffects();
		}
		if ((origin.distanceSquared(player.getLocation()) > radius * radius) || (System.currentTimeMillis() > time + duration)) {
			remove();
			return;
		} else if (!player.isSneaking()) {
			targetLoc = GeneralMethods.getTargetedLocation(player, range);
			player.teleport(targetLoc);
			remove();
			return;
		}
	}
	
	public void vanishPlayer() {
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 2), true);
		player.setFireTicks(-1);
		bPlayer.addCooldown(this);
	}
	
	public void playEffects() {
		ParticleEffect.DRAGON_BREATH.display(player.getLocation().add(0, 1, 0), 0, 0, 0, 0.09F, 20);
		ParticleEffect.PORTAL.display(player.getLocation().add(0, 1, 0), 0, 0, 0, 2F, 30);
		Methods.playSpiritParticles(bPlayer, player.getLocation().add(0, 1, 0), 0.5F, 0.5f, 0.5F, 0, 10);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.5F, -1);
	}
	
	@Override
	public void remove() {
		playEffects();
		if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
		super.remove();
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

		return "Vanish";
	}
	
	@Override
	public String getDescription() {
		return Methods.setSpiritDescription(SpiritType.NEUTRAL, "Mobility") +
				ConfigManager.languageConfig.get().getString("Abilities.Spirit.Vanish.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.setSpiritDescriptionColor(SpiritType.NEUTRAL) + ConfigManager.languageConfig.get().getString("Abilities.Spirit.Vanish.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.setSpiritDescriptionColor(SpiritType.NEUTRAL) + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.setSpiritDescriptionColor(SpiritType.NEUTRAL) + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Vanish.Enabled");
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
