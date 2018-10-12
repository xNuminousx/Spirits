package me.xnuminousx.spirits.ability.dark;

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
import me.xnuminousx.spirits.ability.api.DarkAbility;

public class Shackle extends DarkAbility implements AddonAbility {

	private LivingEntity target = null;
	private Location targetLoc;
	private Location location;
	private int range;
	private long time;
	private long duration;
	private Location origin;
	private Vector direction;
	private double radius;
	private int currPoint;
	private boolean progress;
	private long cooldown;

	public Shackle(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, -1);
		start();
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.DarkSpirit.Shackle.Cooldown");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.DarkSpirit.Shackle.Duration");
		this.range = ConfigManager.getConfig().getInt("Abilities.Spirits.DarkSpirit.Shackle.Range");
		this.radius = ConfigManager.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Shackle.Radius");
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.location = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.progress = true;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, origin)) {
			remove();
			return;
		}
		
		if ((origin.distanceSquared(location) > range * range) && target == null) {
			bPlayer.addCooldown(this);
			remove();
			return;
			
		}
		bind();
	}
	
	public void bind() {
		bPlayer.addCooldown(this);
		if (progress) {
			location.add(direction.multiply(1));
			
			blastSpiral(200, 0.04F, location);
		}
		if (target == null) {
			for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, radius)) {
				if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
					target = (LivingEntity) entity;
					targetLoc = entity.getLocation();
				}
			}
		} else {
			if (target.isDead() || target.getWorld() != player.getWorld()) {
				remove();
				return;
			}
			if (System.currentTimeMillis() > time + duration) {
				ParticleEffect.CLOUD.display(targetLoc, 0, 0, 0, 0.08F, 5);
				player.getWorld().playSound(targetLoc, Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 0.5F, 1.5F);
				bPlayer.addCooldown(this);
				remove();
				return;
			} else {
				for (Entity entity : GeneralMethods.getEntitiesAroundPoint(targetLoc, 2)) {
					if (entity != target) {
						target.teleport(targetLoc);
					}
				}
				this.progress = false;
				Vector vec = targetLoc.getDirection().normalize().multiply(0);
				target.setVelocity(vec);
				targetLoc.setPitch(targetLoc.getPitch());
				targetLoc.setYaw(targetLoc.getYaw());
				
				holdSpiral(30, 0.04F, target.getLocation());
			}
		}
	}
	
	public void blastSpiral(int points, float size, Location location) {
		for (int i = 0; i < 6; i++) {
			currPoint += 360 / points;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
			double x = size * (Math.PI * 4 - angle) * Math.cos(angle + i);
            double z = size * (Math.PI * 4 - angle) * Math.sin(angle + i);
			location.add(x, 0.1F, z);
			ParticleEffect.WITCH_MAGIC.display(location, 0, 0, 0, 0, 1);
			location.subtract(x, 0.1F, z);
		}
	}
	
	public void holdSpiral(int points, float size, Location location) {
		for (int t = 0; t < 2; t++) {
			currPoint += 360 / points;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle2 = currPoint * Math.PI / 180 * Math.cos(Math.PI);
			double x2 = size * (Math.PI * 5 - angle2) * Math.cos(angle2 + t);
            double z2 = size * (Math.PI * 5 - angle2) * Math.sin(angle2 + t);
			location.add(x2, 0.1F, z2);
			ParticleEffect.WITCH_MAGIC.display(location, 0, 0, 0, 0, 1);
			location.subtract(x2, 0.1F, z2);
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
		return "Shackle";
	}
	
	@Override
	public String getDescription() {
		return Methods.setSpiritDescription(SpiritType.DARK, "Defense") + 
				ConfigManager.languageConfig.get().getString("Abilities.DarkSpirit.Shackle.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.setSpiritDescriptionColor(SpiritType.DARK) + ConfigManager.languageConfig.get().getString("Abilities.DarkSpirit.Shackle.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.setSpiritDescriptionColor(SpiritType.DARK) + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.setSpiritDescriptionColor(SpiritType.DARK) + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Shackle.Enabled");
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