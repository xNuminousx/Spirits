package me.xnuminousx.spirits.ability.spirit;

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
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.SpiritAbility;
import net.md_5.bungee.api.ChatColor;

public class Possess extends SpiritAbility implements AddonAbility {

	private Location location;
	private double range;
	private long time;
	private long duration;
	private double damage;
	private long cooldown;
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
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Possess.Cooldown");
		this.range = ConfigManager.getConfig().getDouble("Abilities.Spirits.Neutral.Possess.Radius");
		this.damage = ConfigManager.getConfig().getDouble("Abilities.Spirits.Neutral.Possess.Damage");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Possess.Duration");
		this.location = player.getLocation().clone();
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
			possess();
		} else {
			remove();
			return;
		}

	}
	
	public void possess() {
		
		Entity enemy = GeneralMethods.getTargetedEntity(player, range);
		if ((enemy instanceof Player) && enemy.getUniqueId() != player.getUniqueId()) {
			Location location = enemy.getLocation();
			if (System.currentTimeMillis() > time + duration) {
				DamageHandler.damageEntity(enemy, damage, this);
				player.getWorld().playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.5F);
				remove();
				return;
			} else {
				bPlayer.addCooldown(this);
				
				// Teleport player
				location.setPitch(location.getPitch());
				location.setYaw(location.getYaw());
				player.teleport(location);
				
				// Grab target
				Vector vec = location.getDirection().normalize().multiply(0);
				enemy.setVelocity(vec);
				player.getWorld().playSound(location, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.5F, 5);
				
				// Possession effects
				LivingEntity lenemy = (LivingEntity)enemy;
				ParticleEffect.DRAGON_BREATH.display(location, 0.3F, 1F, 0.3F, 0.02F, 1);
				lenemy.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2), true);
				lenemy.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2), true);
				
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 2), true);
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
		return Methods.getSpiritDescription("spirit", "Offense") +
				ConfigManager.languageConfig.get().getString("Abilities.Spirit.Possess.Description");
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.BLUE + ConfigManager.languageConfig.get().getString("Abilities.Spirit.Possess.Instructions");
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
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Possess.Enabled");
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
