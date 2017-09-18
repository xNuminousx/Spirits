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
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Abilities.SpiritAbility;
import me.xnuminousx.spirits.Listeners.AbilityListener;
import net.md_5.bungee.api.ChatColor;

public class Possess extends SpiritAbility implements AddonAbility {

	private Location location;
	private double range;
	private long time;
	private long duration;
	private double damage;
	private long cooldown;
	

	public Possess(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		
		start();
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.Spirits.Possess.Cooldown");
		this.range = ConfigManager.getConfig().getDouble("ExtraAbilities.Spirits.Possess.Radius");
		this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.Spirits.Possess.Damage");
		this.duration = ConfigManager.getConfig().getLong("ExtraAbilities.Spirits.Possess.Duration");
		this.location = player.getLocation();
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
			remove();
			return;
		}
		
		if (!bPlayer.getBoundAbilityName().equals(getName())) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		
		if (player.isSneaking()) {
			player.getWorld().playSound(location, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.5F, 5);
			possess();
		} else {
			remove();
			return;
		}

	}
	
	public void possess() {
		
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(location, range)) {
			if (((target instanceof LivingEntity)) && (target.getEntityId() != player.getEntityId())) {
				Location location = player.getLocation();
				if (System.currentTimeMillis() > time + duration) {
					DamageHandler.damageEntity(target, damage, this);
					player.getWorld().playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.5F);
					remove();
					return;
				} else {
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
		bPlayer.addCooldown(this);
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
		return ChatColor.BLUE + "" + ChatColor.BOLD + "Offensive: " + ChatColor.DARK_AQUA + "A very ancient ability of spitits is the ability to jump inside the body of a human. As a spirit, you'll be able to possess a human for a short amount of time and do harm. While this ability is active, both the Spirit and the target will be motionless.";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.BLUE + "Hold shift to possess near by humans.";
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
