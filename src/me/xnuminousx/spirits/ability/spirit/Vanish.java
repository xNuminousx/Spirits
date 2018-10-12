package me.xnuminousx.spirits.ability.spirit;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
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
	private long chargeTime;
	private long duration;
	private boolean removeFire;
	private boolean isCharged;
	private Location origin;
	private Location targetLoc;
	private long range;
	private long radius;
	private boolean applyInvis = true;
	private int particleFrequency;
	private boolean doHalfEffect;
	private double healthReq;
	private int divideFactor;
	private World originWorld;

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
		this.chargeTime = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Vanish.ChargeTime");
		this.radius = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Vanish.Radius");
		this.particleFrequency = ConfigManager.getConfig().getInt("Abilities.Spirits.Neutral.Vanish.ParticleFrequency");
		this.removeFire = ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Vanish.RemoveFire");
		
		this.doHalfEffect = ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Vanish.DivideRange.Enabled");
		this.healthReq = ConfigManager.getConfig().getDouble("Abilities.Spirits.Neutral.Vanish.DivideRange.HealthRequired");
		this.divideFactor = ConfigManager.getConfig().getInt("Abilities.Spirits.Neutral.Vanish.DivideRange.DivideFactor");
		
		if (doHalfEffect && player.getHealth() < healthReq) {
			this.range = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Vanish.Range") / divideFactor;
		} else {
			this.range = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Vanish.Range");
		}
		this.origin = player.getLocation();
		this.isCharged = false;
		originWorld = player.getWorld();

	}

	@Override
	public void progress() {
		Methods.readGeneralMethods(this, player, originWorld);
		
		if (!isCharged) {
			if (player.isSneaking()) {
				if (System.currentTimeMillis() > time + chargeTime) {
					isCharged = true;
				} else {
					if (new Random().nextInt(particleFrequency) == 0) {
						ParticleEffect.DRAGON_BREATH.display(player.getLocation().add(0, 1, 0), 0, 0, 0, 0.09F, 1);
					}
				}
			} else {
				remove();
				return;
			}
		} else {
			if (player.isSneaking()) {
				playEffects();
				
				if ((origin.distanceSquared(player.getLocation()) > radius * radius) || (System.currentTimeMillis() > time + duration)) {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5F, -1);
					remove();
					return;
				}
			} else {
				vanishPlayer();
				targetLoc = GeneralMethods.getTargetedLocation(player, range);
				player.teleport(targetLoc);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5F, -1);
				ParticleEffect.DRAGON_BREATH.display(player.getLocation().add(0, 1, 0), 0, 0, 0, 0.09F, 20);
				ParticleEffect.PORTAL.display(player.getLocation().add(0, 1, 0), 0, 0, 0, 2F, 30);
				Methods.playSpiritParticles(bPlayer, player.getLocation().add(0, 1, 0), 0.5F, 0.5f, 0.5F, 0, 10);
				remove();
				return;
			}
		}
	}
	
	public void vanishPlayer() {
		if (removeFire) {
			player.setFireTicks(-1);
		}
		bPlayer.addCooldown(this);
	}
	
	public void playEffects() {
		if (new Random().nextInt(particleFrequency) == 0) {
			Methods.playSpiritParticles(bPlayer, player.getLocation().add(0, 1, 0), 0.5F, 0.5f, 0.5F, 0, 2);
		}
		if (applyInvis) {
			applyInvis = false;
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) duration, 2), true);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5F, -1);
		}
	}
	
	@Override
	public void remove() {
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