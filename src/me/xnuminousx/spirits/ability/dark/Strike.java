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
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.ability.api.DarkAbility;
import net.md_5.bungee.api.ChatColor;

public class Strike extends DarkAbility implements AddonAbility {

	private long cooldown;
	private boolean enable;
	private boolean isHidden;
	private Location origin;
	private Location location;
	private int range;
	private Vector direction;
	private boolean progress;
	private double damage;

	public Strike(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		start();
	}

	private void setFields() {
		this.enable = ConfigManager.getConfig().getBoolean("ExtraAbilities.DarkSpirit.Strike.Enable");
		this.cooldown = ConfigManager.getConfig().getLong("ExtraAbilities.DarkSpirit.Strike.Cooldown");
		this.damage = ConfigManager.getConfig().getDouble("ExtraAbilities.DarkSpirit.Strike.Damage");
		this.range = ConfigManager.getConfig().getInt("ExtraAbilities.DarkSpirit.Strike.Range");
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
		
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, player.getLocation())) {
			remove();
			return;
		}
		
		if (origin.distanceSquared(location) > range * range) {
			remove();
			return;
			
		}
		strike();

	}
	
	public void strike() {
		if (progress) {
			location.add(direction.multiply(1));
			ParticleEffect.CRIT.display(location, 0, 0, 0, 0, 1);
		}
		
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(location, 1.5)) {
			if (((target instanceof LivingEntity)) && (target.getEntityId() != player.getEntityId())) {
				Location location = player.getLocation();
				progress = false;
				bPlayer.addCooldown(this);
				
				LivingEntity le = (LivingEntity)target;
				Location tLoc = le.getLocation().clone();
				tLoc.setPitch(location.getPitch());
				tLoc.setYaw(location.getYaw());
				player.teleport(tLoc);
				
				DamageHandler.damageEntity(target, damage, this);
				player.getWorld().playSound(location, Sound.ENTITY_PLAYER_BURP, 0.2F, 0.2F);
				
				bPlayer.addCooldown(this);
				remove();
				return;
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
		return "Strike";
	}
	
	@Override
	public String getDescription() {
		return ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Offense: " + ChatColor.DARK_RED + "The most basic ability of an aggressive, unbalanced Spirit is to rush towards their enemy and try to bite them in 1 swift motion. When you activate this ability, you'll see your target zone. If your target zone gets in range of another entity, you'll be rushed over to them an deal damage.";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.DARK_GRAY + "Left-Click to trigger target spectacle";
	}

	@Override
	public String getAuthor() {
		return ChatColor.DARK_GRAY + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return ChatColor.DARK_GRAY + "1.0";
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

	}

	@Override
	public void stop() {

	}

}
