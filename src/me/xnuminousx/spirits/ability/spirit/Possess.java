package me.xnuminousx.spirits.ability.spirit;

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
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.Methods.SpiritType;
import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class Possess extends SpiritAbility implements AddonAbility {

	private LivingEntity target = null;
	private double range;
	private long time;
	private long duration;
	private double damage;
	private long cooldown;
	private boolean progress;
	private Vector direction;
	private Location entityCheck;
	private Location origin;
	private boolean allowPossession;
	

	public Possess(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.2F, 1);
		start();
	}

	private void setFields() {
		this.cooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Possess.Cooldown");
		this.range = Main.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Possess.Radius");
		this.damage = Main.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Possess.Damage");
		this.duration = Main.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Possess.Duration");
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.entityCheck = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.progress = true;
		this.allowPossession = true;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, player.getLocation()) || origin.distanceSquared(entityCheck) > range * range) {
			remove();
			return;
		}
		
		if (bPlayer.isParalyzed()) {
			remove();
			return;
		}
		
		if (!bPlayer.getBoundAbilityName().equals(getName())) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		
		if (player.isSneaking()) {
			checkEntities();
		} else {
			remove();
			return;
		}

	}
	
	public void checkEntities() {
		if (progress) {
			entityCheck.add(direction.multiply(1));
		}
		if (target == null) {
			for (Entity entity : GeneralMethods.getEntitiesAroundPoint(entityCheck, 1.5)) {
				if ((entity instanceof LivingEntity) && entity.getUniqueId() != player.getUniqueId()) {
					target = (LivingEntity) entity;
				}
			}
		} else {
			progress = false;
			entityCheck = target.getLocation();
			if (target instanceof Player) {
				Player tPlayer = (Player)target;
				if (tPlayer.isFlying()) {
					tPlayer.setFlying(false);
				}
			}
			if (System.currentTimeMillis() > time + duration) {
				DamageHandler.damageEntity(target, damage, this);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.5F);
				remove();
				return;
			} else {
				this.possess(target, player.getLocation());
			}
		}
}
	
	public void possess(LivingEntity target, Location location) {
		if (allowPossession) {
			bPlayer.addCooldown(this);
			Location targetLoc = target.getLocation().clone();
			
			// Teleport player
			targetLoc.setPitch(location.getPitch());
			targetLoc.setYaw(location.getYaw());
			player.teleport(targetLoc);
			
			// Grab target
			Vector vec = targetLoc.getDirection().normalize().multiply(0);
			target.setVelocity(vec);
			if (new Random().nextInt(10) == 0) {
				player.getWorld().playSound(targetLoc, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.1F, 2);
			}
			
			// Possession effects
			ParticleEffect.DRAGON_BREATH.display(targetLoc, 0.3F, 1F, 0.3F, 0.02F, 1);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2), true);
			target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2), true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 2), true);
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
		return "Possess";
	}
	
	@Override
	public String getDescription() {
		return Methods.setSpiritDescription(SpiritType.NEUTRAL, "Offense") +
				Main.plugin.getConfig().getString("Language.Abilities.Spirit.Possess.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.setSpiritDescriptionColor(SpiritType.NEUTRAL) + Main.plugin.getConfig().getString("Language.Abilities.Spirit.Possess.Instructions");
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
		return Main.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Possess.Enabled");
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
		return true;
	}

	@Override
	public void load() {
	}

	@Override
	public void stop() {

	}

}