package me.xnuminousx.spirits.ability.dark.combo;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.DarkAbility;

public class Infest extends DarkAbility implements ComboAbility, AddonAbility {

	private boolean progress;
	private long cooldown;
	private Location location;
	private Location origin;
	private Vector direction;
	private int currPoint;
	private int range;
	private double radius;
	private long time;
	private long duration;
	private boolean firstEff;

	public Infest(Player player) {
		super(player);
		
		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}
		
		setFields();
		start();
		time = System.currentTimeMillis();
		bPlayer.addCooldown(this);
	}

	private void setFields() {
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.location = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.progress = true;
		this.firstEff = true;
		this.range = 20;
		this.radius = 2;
		this.duration = 5000;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location) || !bPlayer.canBendIgnoreBindsCooldowns(this)) {
			remove();
			return;
		}
		if (origin.distanceSquared(location) > range * range) {
			bPlayer.addCooldown(this);
			remove();
			return;
			
		}
		swarm(20, 0.02F);
	}
	
	public void swarm(int points, float size) {
		if (progress) {
			location.add(direction.multiply(1));
			for (int i = 0; i < 6; i++) {
				currPoint += 360 / points;
				if (currPoint > 360) {
					currPoint = 0;
				}
				double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
				double x = size * (Math.PI * 4 - angle) * Math.cos(angle + i);
				double y = 0.6 * Math.cos(angle);
	            double z = size * (Math.PI * 4 - angle) * Math.sin(angle + i);
				location.add(x, y, z);
				ParticleEffect.WITCH_MAGIC.display(location, 0.6F, 0.6F, 0.6F, 0, 1);
				ParticleEffect.SMOKE.display(location, 0, 0, 0, 0, 1);
				location.subtract(x, y, z);
			}
		}
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(location, radius)) {
			if (target instanceof LivingEntity && !target.getUniqueId().equals(player.getUniqueId())) {
				this.progress = false;
				location = target.getLocation();
				
				if (System.currentTimeMillis() > time + duration) {
					remove();
					return;
				} else {
					ParticleEffect.WITCH_MAGIC.display(location, 1, 1, 1, 0, 2);
					ParticleEffect.SMOKE.display(location, 0.5F, 1, 0.5F, 0, 5);
					
					if (System.currentTimeMillis() > time + (duration / 4) && (firstEff)) {
						ParticleEffect.DRAGON_BREATH.display(location, 0, 0, 0, 0.2F, 1);
					}
					
					if (System.currentTimeMillis() > time + (duration / 2)) {
						firstEff = false;
						ParticleEffect.FLAME.display(location, 0, 0, 0, 0.2F, 2);
					}
					
					//Remove swarm
				}
			}
		}
	}

	@Override
	public Object createNewComboInstance(Player player) {
		return new Infest(player);
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		ArrayList<AbilityInformation> combo = new ArrayList<>();
		combo.add(new AbilityInformation("Intoxicate", ClickType.SHIFT_DOWN));
		combo.add(new AbilityInformation("Strike", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Strike", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Strike", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Strike", ClickType.SHIFT_UP));
		return combo;
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
		return "Infest";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("dark", "Combo", "A very dangerous combo; used in offense to attack players and infest them with the influence of darkness.");
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.DARK_GRAY + "Intoxicate (Tap shift) > Strike (Tap-Shift) > Strike (Left-click x3)";
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
