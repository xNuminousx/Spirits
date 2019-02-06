package me.xnuminousx.spirits.ability.light;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.Collision;
import com.projectkorra.projectkorra.airbending.AirSwipe;
import com.projectkorra.projectkorra.earthbending.EarthBlast;
import com.projectkorra.projectkorra.firebending.FireBlast;
import com.projectkorra.projectkorra.firebending.FireBlastCharged;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.waterbending.WaterManipulation;

import me.xnuminousx.spirits.Main;
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

	private Location shieldLocation;

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
		this.othersCooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Others.Cooldown");
		this.selfCooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Self.Cooldown");
		this.duration = Main.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Duration");
		this.range = Main.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.Range");
		this.clickDelay = Main.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Others.ClickDelay");
		this.shieldSize = Main.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.Others.ShieldSize");
		this.selfShield = Main.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.Self.ShieldSize");
		this.knockDis = Main.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Others.KnockbackPower");
		this.selfKnockDis = Main.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Self.KnockbackPower");
		this.removeOnDamage = Main.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirits.Shelter.RemoveOnDamage");
		this.removeIfFar = Main.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Shelter.RemoveIfFarAway.Enabled");
		this.removeDistance = Main.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.RemoveIfFarAway.Distance");
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
			blockMove();
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
					blockMove();
					rotateShield(location, 100, shieldSize);
					shieldLocation = location;
					
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
			ParticleEffect.SPELL_INSTANT.display(location, 1, 0.5F, 0.5F, 0.5F, 0);
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
			ParticleEffect.SPELL_INSTANT.display(location, 1, 0, 0, 0, 0);
			location.subtract(x, 0.1F, z);
		}
	}
	
	private static void blockMove() {
		CoreAbility fireBlast = CoreAbility.getAbility(FireBlast.class);
		CoreAbility earthBlast = CoreAbility.getAbility(EarthBlast.class);
		CoreAbility waterManip = CoreAbility.getAbility(WaterManipulation.class);
		CoreAbility airSwipe = CoreAbility.getAbility(AirSwipe.class);
		CoreAbility fireBlastCharged = CoreAbility.getAbility(FireBlastCharged.class);
		
		CoreAbility shelter = CoreAbility.getAbility(Shelter.class);
		
		CoreAbility[] smallAbilities = { airSwipe, earthBlast, waterManip, fireBlast, fireBlastCharged };
		
		for (CoreAbility smallAbil : smallAbilities) {
			ProjectKorra.getCollisionManager().addCollision(new Collision(shelter, smallAbil, false, true));
		}
	}

	@Override
	public long getCooldown() {
		return shelterType == ShelterType.CLICK ? othersCooldown : selfCooldown;
	}

	@Override
	public Location getLocation() {
		return shelterType == ShelterType.CLICK ?  shieldLocation : player.getLocation();
	}
	
	@Override
	public double getCollisionRadius() {
		return shelterType == ShelterType.CLICK ? shieldSize : selfShield;
	}

	@Override
	public String getName() {
		return "Shelter";
	}
	
	@Override
	public String getDescription() {
		return Methods.setSpiritDescription(SpiritType.LIGHT, "Defense") +
				Main.plugin.getConfig().getString("Language.Abilities.LightSpirit.Shelter.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.setSpiritDescriptionColor(SpiritType.LIGHT) + Main.plugin.getConfig().getString("Language.Abilities.LightSpirit.Shelter.Instructions");
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
		return Main.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Shelter.Enabled");
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