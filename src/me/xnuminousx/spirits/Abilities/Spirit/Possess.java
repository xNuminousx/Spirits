package me.xnuminousx.spirits.Abilities.Spirit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.Abilities.API.SpiritAbility;
import me.xnuminousx.spirits.Listeners.AbilityListener;
import net.md_5.bungee.api.ChatColor;

public class Possess extends SpiritAbility implements AddonAbility {

	private Location location;
	private double range;
	private long time;
	private long duration;
	private double damage;
	private long cooldown;
	private boolean enable;
	private boolean isHidden;
	private boolean progress;
	private Vector direction;
	private Location origin;
	

	public Possess(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.5F, 5);
		
		start();
	}

	private void setFields() {
		this.enable = Main.plugin.getConfig().getBoolean("Abilities.Spirits.Possess.Enable");
		this.cooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.Possess.Cooldown");
		this.range = Main.plugin.getConfig().getDouble("Abilities.Spirits.Possess.Radius");
		this.damage = Main.plugin.getConfig().getDouble("Abilities.Spirits.Possess.Damage");
		this.duration = Main.plugin.getConfig().getLong("Abilities.Spirits.Possess.Duration");
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.location = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.isHidden = false;
		this.progress = true;
	}

	@Override
	public void progress() {
		if (!enable) {
			isHidden = true;
			remove()	;
			return;
		}
		
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
			remove();
			return;
		}
		
		if (origin.distanceSquared(location) > range * range) {
			remove();
			return;
			
		}
		
		if (!bPlayer.getBoundAbilityName().equals(getName())) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		
		if (player.isSneaking()) {
			possess();
		} else {
			remove();
			return;
		}

	}
	
	public void possess() {
		if (progress) {
			location.add(direction.multiply(1));
		}
		
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(location, 2)) {
			if (((target instanceof LivingEntity)) && (target.getEntityId() != player.getEntityId())) {
				Location location = player.getLocation();
				if (System.currentTimeMillis() > time + duration) {
					DamageHandler.damageEntity(target, damage, this);
					player.getWorld().playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.5F);
					remove();
					return;
				} else {
					progress = false;
					bPlayer.addCooldown(this);
					
					// Teleport player
					LivingEntity le = (LivingEntity)target;
					Location tLoc = le.getLocation().clone();
					tLoc.setPitch(location.getPitch());
					tLoc.setYaw(location.getYaw());
					player.teleport(tLoc);
					
					// Grab target
					Location tarLoc = target.getLocation();
					Vector vec = tarLoc.getDirection().normalize().multiply(0);
					target.setVelocity(vec);
					player.getWorld().playSound(tarLoc, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.5F, 5);
					
					// Possession effects
					ParticleEffect.DRAGON_BREATH.display(tLoc, 0.3F, 1F, 0.3F, 0.02F, 5);
					le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2), true);
					le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2), true);
					
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 2), true);
				}
			}
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
		return ChatColor.BLUE + "" + ChatColor.BOLD + "Offense: " + ChatColor.DARK_AQUA + "A very ancient ability of spitits is the ability to jump inside the body of a human. As a spirit, you'll be able to possess a human for a short amount of time and do harm. While this ability is active, both the Spirit and the target will be motionless.";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.BLUE + "Hold shift to possess the closest entity you're looking at.";
	}

	@Override
	public String getAuthor() {
		return ChatColor.BLUE + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return ChatColor.BLUE + "1.0";
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
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new AbilityListener(), ProjectKorra.plugin);
	}

	@Override
	public void stop() {
		super.remove();

	}

}
