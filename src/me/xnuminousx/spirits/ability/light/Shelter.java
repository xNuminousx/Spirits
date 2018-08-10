package me.xnuminousx.spirits.ability.light;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.Methods.SpiritType;
import me.xnuminousx.spirits.ability.api.LightAbility;

public class Shelter extends LightAbility implements AddonAbility {
	
	public enum ShelterType {
		CLICK, SHIFT
	}
	public ShelterType shelterType;

	private boolean isDamaged;
	private boolean removeOnDamage;
	private double startHealth;
	private Location location;
	private int range;
	private long time;
	private long duration;
	private Location origin;
	private Vector direction;
	private int currPoint;
	private boolean progress;
	private long othersCooldown;
	private long selfCooldown;
	private float shieldSize;
	private float selfShield;
	private long knockDis;
	private long selfKnockDis;
	private long clickDelay;
	private boolean removeIfFar;
	private int removeDistance;

	public Shelter(Player player, ShelterType shelterType) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		
		time = System.currentTimeMillis();
		this.shelterType = shelterType;
		startHealth = player.getHealth();
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.1F, 2);
		
		start();
	}

	private void setFields() {
		this.othersCooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Others.Cooldown");
		this.selfCooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Self.Cooldown");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Duration");
		this.range = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.Range");
		this.clickDelay = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Others.ClickDelay");
		this.shieldSize = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.Others.ShieldSize");
		this.selfShield = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.Self.ShieldSize");
		this.knockDis = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Others.KnockbackPower");
		this.selfKnockDis = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Self.KnockbackPower");
		this.removeOnDamage = ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Shelter.RemoveOnDamage");
		this.removeIfFar = ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Shelter.RemoveIfFarAway.Enabled");
		this.removeDistance = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.RemoveIfFarAway.Distance");
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.location = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.progress = true;
		this.isDamaged = false;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location) || origin.distanceSquared(location) > range * range) {
			remove();
			return;
		}
		
		if (removeOnDamage) {
			if (player.getHealth() < startHealth) {
				isDamaged = true;
				
			}
		}
		
		if (shelterType == ShelterType.CLICK) {
			shieldOther();
		} else if (shelterType == ShelterType.SHIFT) {
			if (player.isSneaking()) {
				shieldSelf();
			} else {
				bPlayer.addCooldown(this, selfCooldown);
				remove();
				return;
			}
		}
	}
	
	public void shieldSelf() {
		if (System.currentTimeMillis() > time + duration) {
			bPlayer.addCooldown(this, selfCooldown);
			remove();
			return;
		} else {
			rotateShield(player.getLocation(), 96, selfShield);
			for (Entity target : GeneralMethods.getEntitiesAroundPoint(player.getLocation(), selfShield)) {
				if (target instanceof LivingEntity && !target.getUniqueId().equals(player.getUniqueId())) {
					Vector vec = target.getLocation().getDirection().normalize().multiply(-selfKnockDis);
					vec.setY(1);
					target.setVelocity(vec);
				}
			}
		}
	}
	
	public void shieldOther() {
		if (progress) {
			location.add(direction.multiply(1));
			progressBlast(location, 100, 0.04F);
		}
		
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(location, 2)) {
			if (target instanceof LivingEntity && !target.getUniqueId().equals(player.getUniqueId())) {
				bPlayer.addCooldown(this, othersCooldown);
				if (System.currentTimeMillis() > time + duration) {
					remove();
					return;
				} else {
					this.progress = false;
					location = target.getLocation();
					
					if (isDamaged) {
						remove();
						return;
					}
					for (Entity target2 : GeneralMethods.getEntitiesAroundPoint(location, shieldSize)) {
						if (target2 instanceof LivingEntity && !target2.getUniqueId().equals(target.getUniqueId()) && !target2.getUniqueId().equals(player.getUniqueId())) {
							Vector vec = target2.getLocation().getDirection().normalize().multiply(-knockDis);
							vec.setY(1);
							target2.setVelocity(vec);
						}
					}
					rotateShield(location, 100, shieldSize);
					if (removeIfFar) {
						if (player.getLocation().distanceSquared(target.getLocation()) > removeDistance * removeDistance) {
							remove();
							return;
						}
					}
				}
			} else {
				bPlayer.addCooldown(this, clickDelay);
			}
		}
	}
	public void rotateShield(Location location, int points, float size) {
		for (int t = 0; t < 6; t++) {
			currPoint += 360 / points;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
			double x2 = size * Math.cos(angle);
			double y = 0.9 * (Math.PI * 5 - t) - 10;
            double z2 = size * Math.sin(angle);
			location.add(x2, y, z2);
			ParticleEffect.INSTANT_SPELL.display(location, 0.5F, 0.5F, 0.5F, 0, 1);
			location.subtract(x2, y, z2);
		}
	}
	public void progressBlast(Location location, int points, float size) {
		for (int i = 0; i < 6; i++) {
			currPoint += 360 / points;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
			double x = size * (Math.PI * 4 - angle) * Math.cos(angle + i);
            double z = size * (Math.PI * 4 - angle) * Math.sin(angle + i);
			location.add(x, 0.1F, z);
			ParticleEffect.INSTANT_SPELL.display(location, 0, 0, 0, 0, 1);
			location.subtract(x, 0.1F, z);
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
		return "Shelter";
	}
	
	@Override
	public String getDescription() {
		return Methods.setSpiritDescription(SpiritType.LIGHT, "Defense") +
				ConfigManager.languageConfig.get().getString("Abilities.LightSpirit.Shelter.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.setSpiritDescriptionColor(SpiritType.LIGHT) + ConfigManager.languageConfig.get().getString("Abilities.LightSpirit.Shelter.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.setSpiritDescriptionColor(SpiritType.LIGHT) + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.setSpiritDescriptionColor(SpiritType.LIGHT) + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Shelter.Enabled");
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