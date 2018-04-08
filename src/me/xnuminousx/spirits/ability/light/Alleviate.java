package me.xnuminousx.spirits.ability.light;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.LightAbility;

public class Alleviate extends LightAbility implements AddonAbility {

	private Location location;
	private Location origin;
	private Location entityCheck;
	private Vector direction;
	private int currPoint;
	private int healDuration;
	private int nightVisDuration;
	private double range;
	private long time;
	private long potInt;
	private long healInt;
	private long otherCooldown;
	private long selfCooldown;
	private long chargeTime;
	private String hexColor;
	private String selfHexColor;
	private boolean progress;
	private boolean playingAlleviate;
	private boolean removeNegPots;

	public Alleviate(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		start();
	}

	private void setFields() {
		//Alleviate
		this.otherCooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Others.Cooldown");
		this.range = ConfigManager.getConfig().getDouble("Abilities.Spirits.LightSpirit.Alleviate.Others.Range");
		this.potInt = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Others.PotionInterval");
		this.healInt = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Others.HealInterval");
		this.hexColor = ConfigManager.getConfig().getString("Abilities.Spirits.LightSpirit.Alleviate.Others.ParticleColor (Has to be 6 characters)");
		
		//Sanctity
		this.selfCooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Self.Cooldown");
		this.chargeTime = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Self.ChargeTime");
		this.healDuration = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Alleviate.Self.HealDuration");
		this.nightVisDuration = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Alleviate.Self.NightVisionDuration");
		this.removeNegPots = ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Alleviate.Self.RemoveNegativePotionEffects");
		this.selfHexColor = ConfigManager.getConfig().getString("Abilities.Spirits.LightSpirit.Alleviate.Self.ParticleColor (Has to be 6 characters)");
		
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.location = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.progress = true;
		this.playingAlleviate = false;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
			remove();
			return;
		}
		
		if (!bPlayer.getBoundAbilityName().equals(getName())) {
			remove();
			return;
		}
		if (player.isSneaking()) {
			if (progress) {
				entityCheck = location;
				entityCheck.add(direction.multiply(1));
				// ParticleEffect.FLAME.display(location, 0, 0, 0, 0, 1);
			}
			if (origin.distanceSquared(entityCheck) > range * range) {
				progress = false;
				progressSanctity(200, 0.04F);
			}
			for (Entity target : GeneralMethods.getEntitiesAroundPoint(entityCheck, 1)) {
				if (((target instanceof LivingEntity)) && (target.getEntityId() != player.getEntityId())) {
					progress = false;
					entityCheck = target.getLocation();
					progressAlleviate(200, 0.04F, target, target.getLocation().clone());
				}
			}
		} else {
			remove();
			return;
		}
	}
	
	public void progressAlleviate(int points, float size, Entity target, Location location) {
		playingAlleviate = true;
		LivingEntity le = (LivingEntity)target;
		
		for (int i = 0; i < 6; i++) {
			currPoint += 360 / points;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
			double x = size * (Math.PI * 4 - angle) * Math.cos(angle + i);
            double y = 1.2 * Math.cos(angle) + 1.2;
            double z = size * (Math.PI * 4 - angle) * Math.sin(angle + i);
			location.add(x, y, z);
			GeneralMethods.displayColoredParticle(location, hexColor, 0, 0, 0);
			location.subtract(x, y, z);
		}
		
		if (System.currentTimeMillis() - time > potInt) {
			for (PotionEffect targetEffect : le.getActivePotionEffects()) {
				if (isNegativeEffect(targetEffect.getType())) {
					le.removePotionEffect(targetEffect.getType());
				}
			}
			bPlayer.addCooldown(this, otherCooldown);
		}
		if (System.currentTimeMillis() - time > healInt) {
			le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1), true);
			DamageHandler.damageEntity(player, 6, this);
			bPlayer.addCooldown(this, otherCooldown);
			remove();
			return;
		}
		if (!player.isSneaking()) {
			bPlayer.addCooldown(this, otherCooldown);
		}
		if (new Random().nextInt(20) == 0) {
			target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDEREYE_DEATH, 1, 1);
		}
	}
	
	public void progressSanctity(int points, float size) {
		if (playingAlleviate) {
			return;
		} else {
			Location location = player.getLocation();
			for (int i = 0; i < 6; i++) {
				currPoint += 360 / points;
				if (currPoint > 360) {
					currPoint = 0;
				}
				double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
				double x = size * (Math.PI * 4 - angle) * Math.cos(angle + i);
	            double y = 1.2 * Math.cos(angle) + 1.2;
	            double z = size * (Math.PI * 4 - angle) * Math.sin(angle + i);
				location.add(x, y, z);
				GeneralMethods.displayColoredParticle(location, selfHexColor, 0, 0, 0);
				location.subtract(x, y, z);
			}
			if (System.currentTimeMillis() > time + chargeTime) {
				for (PotionEffect playerEffects : player.getActivePotionEffects()) {
					if (isNegativeEffect(playerEffects.getType()) && removeNegPots) {
						player.removePotionEffect(playerEffects.getType());
					}
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, healDuration * 100, 1), true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, nightVisDuration * 100, 1));
				
				bPlayer.addCooldown(this, selfCooldown);
				remove();
				return;
			}
		}
		if (!player.isSneaking()) {
			bPlayer.addCooldown(this, selfCooldown);
		}
		if (new Random().nextInt(20) == 0) {
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDEREYE_DEATH, 1, 1);
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
		return "Alleviate";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("light", "Utility") +
				ConfigManager.languageConfig.get().getString("Abilities.LightSpirit.Alleviate.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.spiritChatColor("light") + ConfigManager.languageConfig.get().getString("Abilities.LightSpirit.Alleviate.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.spiritChatColor("light") + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.spiritChatColor("light") + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Alleviate.Enabled");
	}

	@Override
	public boolean isExplosiveAbility() {
		return false;
	}

	@Override
	public boolean isHarmlessAbility() {
		return true;
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