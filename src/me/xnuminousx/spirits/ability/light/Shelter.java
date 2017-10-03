package me.xnuminousx.spirits.ability.light;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.LightAbility;
import net.md_5.bungee.api.ChatColor;

public class Shelter extends LightAbility implements AddonAbility {
	
	private boolean enable;
	private boolean isHidden;
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
	private long cooldown;
	private float shieldSize;
	private long knockDis;

	public Shelter(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		
		time = System.currentTimeMillis();
		startHealth = player.getHealth();
		
		start();
	}

	private void setFields() {
		this.enable = ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Shelter.Enable");
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Cooldown");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Duration");
		this.range = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.Range");
		this.shieldSize = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.ShieldSize");
		this.knockDis = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.KnockbackPower");
		this.removeOnDamage = ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirits.Shelter.RemoveOnDamage");
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.location = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.isHidden = false;
		this.progress = true;
		this.isDamaged = false;
	}

	@Override
	public void progress() {
		if (!enable) {
			isHidden = true;
			remove()	;
			return;
		}
		
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location) || origin.distanceSquared(location) > range * range) {
			remove();
			return;
		}
		
		if (removeOnDamage) {
			if (player.getHealth() < startHealth) {
				isDamaged = true;
				
			}
		}
		
		shield(100, 100, 0.04F, shieldSize);
	}
	
	public void shield(int points, int points2, float size, float size2) {
		bPlayer.addCooldown(this);
		if (progress) {
			location.add(direction.multiply(1));
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
		
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(location, 2)) {
			if (target instanceof LivingEntity && !target.getUniqueId().equals(player.getUniqueId())) {
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
						if (target2 instanceof LivingEntity && !target2.getUniqueId().equals(target.getUniqueId())) {
							Vector vec = target2.getLocation().getDirection().normalize().multiply(-knockDis);
							vec.setY(1);
							target2.setVelocity(vec);
						}
					}
					
					for (int t = 0; t < 6; t++) {
						currPoint += 360 / points2;
						if (currPoint > 360) {
							currPoint = 0;
						}
						double angle2 = currPoint * Math.PI / 180 * Math.cos(Math.PI);
						double x2 = size2 * Math.cos(angle2);
						double y = 0.9 * (Math.PI * 5 - t) - 10;
			            double z2 = size2 * Math.sin(angle2);
						location.add(x2, y, z2);
						ParticleEffect.INSTANT_SPELL.display(location, 0.5F, 0.5F, 0.5F, 0, 1);
						location.subtract(x2, y, z2);
					}
				}
			}
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
		return "Shelter";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("light", "Defense", "A very useful tactic when group battling, a light spirit can temporarily shield a friend or even a foe from incoming enemies.");
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.AQUA + "Left click";
	}

	@Override
	public String getAuthor() {
		return ChatColor.AQUA + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return ChatColor.AQUA + "1.0";
	}
	
	@Override
	public boolean isHiddenAbility() {
		return isHidden;
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
		return false;
	}

	@Override
	public void load() {

	}

	@Override
	public void stop() {

	}

}
