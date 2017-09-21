package me.xnuminousx.spirits.ability.light;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.ability.api.LightAbility;

public class Shelter extends LightAbility implements AddonAbility {

	private long cooldown;
	private long time;
	private Location location;
	private boolean enable;
	private Location origin;
	private Vector direction;
	private boolean isHidden;
	private boolean progress;
	private int range;
	private int currPoint;

	public Shelter(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		
		start();
	}

	private void setFields() {
		this.enable = Main.plugin.getConfig().getBoolean("Abilities.LightSpirit.Shelter.Enable");
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
			bPlayer.addCooldown(this);
			remove();
			return;
			
		}
		shield(200, 30, 0.04F);
	}
	
	public void shield(int points, int points2, float size) {
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
		return "Nothing yet";
	}
	
	@Override
	public String getInstructions() {
		return "Nothing yet";
	}

	@Override
	public String getAuthor() {
		return "xNuminousx";
	}

	@Override
	public String getVersion() {
		return "1.0";
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
