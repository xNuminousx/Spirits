package me.xnuminousx.spirits.ability.light;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.LightAbility;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Sanctity extends LightAbility implements AddonAbility {

	private long cooldown;
	private int duration;
	private int currPoint;
	private long chargeTime;
	private int healeffect;
	private boolean isCharged;

	public Sanctity(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}

		start();
		setFields();
	}

	public void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Sanctity.Cooldown");
		this.healeffect = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Sanctity.Power");
		this.duration = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Sanctity.PotionDuration");
		this.chargeTime = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Sanctity.ChargeTime");
		this.isCharged = false;
	}

	public long getCooldown() {
		return this.cooldown;
	}

	public Location getLocation() {
		return null;
	}

	public boolean isHarmlessAbility() {
		return true;
	}

	public boolean isSneakAbility() {
		return true;
	}

	public void progress() {
		if (player.isDead() || !player.isOnline()
				|| GeneralMethods.isRegionProtectedFromBuild(this, player.getLocation())) {
			remove();
			return;
		}
		
		if (!bPlayer.getBoundAbilityName().equals(getName())) {
			remove();
			return;
		}

		if (this.player.isSneaking()) {
			powerRing(60, 1F, 2);
			if (((System.currentTimeMillis() > getStartTime() + this.chargeTime))) {
				this.isCharged = true;
				haloRing(60, 0.4F, 2);
				ParticleEffect.FIREWORKS_SPARK.display(player.getLocation(), 0.5f, 0.5f, 0.5f, 0.0f, 5);
			}
		} else {
			if (this.isCharged) {
				heal();
				this.player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 2.0F,
						0.1F);
			}
			remove();
			return;
		}
	} 

	private void powerRing(int points, float size, int speed) {
		for (int i = 0; i < speed; i++) {
			this.currPoint += 360 / points;
			if (this.currPoint > 360) {
				this.currPoint = 0;
			}
			double angle = this.currPoint * Math.PI / 180.0D;
			double x = size * Math.cos(angle);
			double x2 = size * Math.sin(angle);
			double z = size * Math.sin(angle);
			double z2 = size * Math.cos(angle);
			Location loc2 = this.player.getLocation().add(x2, 1D, z2);
			GeneralMethods.displayColoredParticle(loc2, "#00FFFF", 0F, 0F, 0F);
			Location loc = this.player.getLocation().add(x, 1D, z);
			GeneralMethods.displayColoredParticle(loc, "#FFFF00", 0F, 0F, 0F);
		}
	}

	private void haloRing(int points, float size, int speed) {
		for (int i = 0; i < speed; i++) {
			this.currPoint += 360 / points;
			if (this.currPoint > 360) {
				this.currPoint = 0;
			}
			double angle = this.currPoint * Math.PI / 180.0D;
			double x = size * Math.cos(angle);
			double x2 = size * Math.sin(angle);
			double z = size * Math.sin(angle);
			double z2 = size * Math.cos(angle);
			Location loc2 = this.player.getLocation().add(x2, 2D, z2);
			GeneralMethods.displayColoredParticle(loc2, "#32CD32", 0F, 0F, 0F);
			Location loc = this.player.getLocation().add(x, 2D, z);
			GeneralMethods.displayColoredParticle(loc, "#FFFFFF", 0F, 0F, 0F);
		}
	}

	public void heal() {
		ParticleEffect.HAPPY_VILLAGER.display(this.player.getLocation(), 0.3F, 1.0F, 0.3F, 1F, 50);
		player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, this.duration * 100, this.healeffect));
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, this.duration * 100, 1));
		player.addPotionEffect(
				new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, this.duration * 100, this.healeffect));
		player.addPotionEffect(
				new PotionEffect(PotionEffectType.FIRE_RESISTANCE, this.duration * 100, this.healeffect));

		bPlayer.addCooldown(this);

	}

	@Override
	public String getName() {
		return "Sanctity";
	}

	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("light", "Defense", "Use this to empower yourself with more health, better resistance and the ability to find the light in the dark for a limited period of time.");
	}

	@Override
	public String getAuthor() {
		return ChatColor.AQUA + "EmeraldJelly";
	}

	@Override
	public String getInstructions() {
		return ChatColor.AQUA + "Hold shift until you see the trigger";
	}

	@Override
	public String getVersion() {
		return ChatColor.AQUA + "v1.0.0";
	}

	public void load() {

	}

	public void stop() {

	}

	@Override
	public boolean isHiddenAbility() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Sanctity.Enable");
	}

	@Override
	public boolean isExplosiveAbility() {
		return false;
	}

	@Override
	public boolean isIgniteAbility() {
		return false;
	}
}