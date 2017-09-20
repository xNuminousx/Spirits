package me.xnuminousx.spirits.ability.light;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.DamageHandler;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.ability.api.LightAbility;
import net.md_5.bungee.api.ChatColor;

public class Alleviate extends LightAbility implements AddonAbility {

	private Location location;
	private Location origin;
	private Vector direction;
	private int currPoint;
	private double range;
	private long time;
	private long potInt;
	private long healInt;
	private long cooldown;
	private String hexColor;
	private boolean enable;
	private boolean isHidden;
	private boolean progress;

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
		this.enable = Main.plugin.getConfig().getBoolean("Abilities.LightSpirit.Alleviate.Enable");
		this.cooldown = Main.plugin.getConfig().getLong("Abilities.LightSpirit.Alleviate.Cooldown");
		this.range = Main.plugin.getConfig().getDouble("Abilities.LightSpirit.Alleviate.Radius");
		this.potInt = Main.plugin.getConfig().getLong("Abilities.LightSpirit.Alleviate.PotionInterval");
		this.healInt = Main.plugin.getConfig().getLong("Abilities.LightSpirit.Alleviate.HealInterval");
		this.hexColor = Main.plugin.getConfig().getString("Abilities.LightSpirit.Alleviate.ParticleColor (Has to be 6 characters)");
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
			effect(200, 0.04F);
			
		} else {
			remove();
			return;
		}
	}
	
	public void effect(int points, float size) {
		if (progress) {
			location.add(direction.multiply(1));
		}
		
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(location, 1)) {
			if (((target instanceof LivingEntity)) && (target.getEntityId() != player.getEntityId())) {
				Location tarLoc = target.getLocation();
				LivingEntity le = (LivingEntity)target;
				progress = false;
				
				for (int i = 0; i < 6; i++) {
					currPoint += 360 / points;
					if (currPoint > 360) {
						currPoint = 0;
					}
					double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
					double x = size * (Math.PI * 4 - angle) * Math.cos(angle + i);
		            double y = 1.2 * Math.cos(angle) + 1.2;
		            double z = size * (Math.PI * 4 - angle) * Math.sin(angle + i);
					tarLoc.add(x, y, z);
					GeneralMethods.displayColoredParticle(tarLoc, hexColor, 0, 0, 0);
					tarLoc.subtract(x, y, z);
				}
				
				if (System.currentTimeMillis() - time > potInt) {
					for (PotionEffect targetEffect : le.getActivePotionEffects()) {
						if (isNegativeEffect(targetEffect.getType())) {
							le.removePotionEffect(targetEffect.getType());
						}
					}
					bPlayer.addCooldown(this);
				}
				if (System.currentTimeMillis() - time > healInt) {
					le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1), true);
					DamageHandler.damageEntity(player, 6, this);
					bPlayer.addCooldown(this);
					remove();
					return;
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
		return "Alleviate";
	}
	
	@Override
	public String getDescription() {
		return ChatColor.AQUA + "" + ChatColor.BOLD + "Utility: " + ChatColor.WHITE + "Use this ability to relieve your friends and allies of their negative potion effects, keep using it and you'll give them a small boost of your own health. If your target moves, the ability will cancel.";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.AQUA + "Hold shift while looking at your target";
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
