package me.xnuminousx.spirits.ability.dark;

import org.bukkit.Location;
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

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.DarkAbility;
import net.md_5.bungee.api.ChatColor;

public class Intoxicate extends DarkAbility implements AddonAbility {

	private Location location;
	private Location origin;
	private Vector direction;
	private int currPoint;
	private double range;
	private long time;
	private long potInt;
	private long harmInt;
	private long cooldown;
	private String hexColor;
	private boolean progress;

	public Intoxicate(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		start();
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.DarkSpirit.Intoxicate.Cooldown");
		this.range = ConfigManager.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Intoxicate.Radius");
		this.potInt = ConfigManager.getConfig().getLong("Abilities.Spirits.DarkSpirit.Intoxicate.PotionInterval");
		this.harmInt = ConfigManager.getConfig().getLong("Abilities.Spirits.DarkSpirit.Intoxicate.HarmInterval");
		this.hexColor = ConfigManager.getConfig().getString("Abilities.Spirits.DarkSpirit.Intoxicate.ParticleColor (Has to be 6 characters)");
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.location = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.progress = true;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location) || origin.distanceSquared(location) > range * range) {
			remove();
			return;
		}
		
		if (!bPlayer.getBoundAbilityName().equals(getName())) {
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
					GeneralMethods.displayColoredParticle(tarLoc, "000000", 0, 0, 0);
					tarLoc.subtract(x, y, z);
				}
				
				if (System.currentTimeMillis() - time > potInt) {
					for (PotionEffect targetEffect : le.getActivePotionEffects()) {
						if (isPositiveEffect(targetEffect.getType())) {
							le.removePotionEffect(targetEffect.getType());
						}
					}
					bPlayer.addCooldown(this);
				}
				if (System.currentTimeMillis() - time > harmInt) {
					le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1), true);
					le.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1000, 1), true);
					DamageHandler.damageEntity(player, 4, this);
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
		return "Intoxicate";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("dark", "Offense") +
				ConfigManager.languageConfig.get().getString("Abilities.DarkSpirit.Intoxicate.Description");
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.DARK_GRAY + ConfigManager.languageConfig.get().getString("Abilities.DarkSpirit.Intoxicate.Instructions");
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
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Intoxicate.Enabled");
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
