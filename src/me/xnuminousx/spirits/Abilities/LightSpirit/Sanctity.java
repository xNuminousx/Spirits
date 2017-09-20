package me.xnuminousx.spirits.Abilities.LightSpirit;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.Abilities.API.LightAbility;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
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

		start();
		setFields();
	}

	public void setFields() {
		this.cooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.Sanctity.Cooldown");
		this.healeffect = Main.plugin.getConfig().getInt("Abilities.Spirits.Sanctity.Power");
		this.duration = Main.plugin.getConfig().getInt("Abilities.Spirits.Sanctity.Duration");
		this.chargeTime = Main.plugin.getConfig().getLong("Abilities.Spirits.Sanctity.ChargeTime");
		this.isCharged = false;
	}

	public long getCooldown() {
		return this.cooldown;
	}

	public Location getLocation() {
		return null;
	}

	public String getName() {
		return "Sanctity"; 
	}

	public String getDescription() {
		return ChatColor.AQUA + "" + ChatColor.BOLD + "Defensive: " + ChatColor.WHITE
				+ "Use this to empower yourself with more health, better resistance and the ability to see in the dark for a limited period of time.";
	}

	public boolean isHarmlessAbility() {
		return true;
	}

	public boolean isSneakAbility() {
		return true;
	}

	public void progress() {
		if (!this.bPlayer.canBend(this)) {
			remove();
			return;
		}
		if (this.player.isSneaking()) {
			powerRing(60, 1.75F, 2);
			if (((System.currentTimeMillis() > getStartTime() + this.chargeTime))) {
				this.isCharged = true; 
			}
		} else {
			if (this.isCharged) {
				if (!this.bPlayer.isOnCooldown(this)) {
					this.bPlayer.addCooldown(this);
				}
				if (!player.isSneaking()) {
					heal();
				}
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
			double angle = this.currPoint * 3.141592653589793D / 180.0D;
			double x = size * Math.cos(angle);
			double z = size * Math.sin(angle);
			Location loc = this.player.getLocation().add(x, 1.0D, z);
			GeneralMethods.displayColoredParticle(loc, "#00FFFF");
			GeneralMethods.displayColoredParticle(loc, "#FFFF00");
		}
	}

	public String getInstructions() {
		return "Hold shift till the ring passes 3 times then release";
	}

	public void heal() {
		ParticleEffect.HAPPY_VILLAGER.display(this.player.getLocation(), 0.3F, 1.0F, 0.3F, 0.0F, 500);
		player
				.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, this.duration * 10, this.healeffect));
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, this.duration * 10, 1));
		player.addPotionEffect(
				new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, this.duration * 10, this.healeffect));
		player.addPotionEffect(
				new PotionEffect(PotionEffectType.FIRE_RESISTANCE, this.duration * 10, this.healeffect));

	}

	public String getAuthor() {
		return "EmeraldJelly";
	}

	public String getVersion() {
		return "v1.0.0";
	}

	public void load() {

	}

	public void stop() {

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
