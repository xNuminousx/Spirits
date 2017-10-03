package me.xnuminousx.spirits.ability.spirit.combo;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class Fuse extends SpiritAbility implements AddonAbility, ComboAbility {

	private boolean enable;
	private boolean isHidden;
	private long cooldown;
	private Location origin;
	private int distance;
	private int strengthDuration;
	private int weaknessDuration;

	public Fuse(Player player) {
		super(player);

		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}
		
		setFields();
		
		start();
	}

	private void setFields() {
		this.origin = player.getLocation();
		this.distance = 10;
		this.strengthDuration = 2;
		this.weaknessDuration = 2;
		this.enable = true;
		this.isHidden = false;
		
	}

	@Override
	public void progress() {
		if (!enable) {
			isHidden = true;
			remove()	;
			return;
		}
		
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, origin)) {
			remove();
			return;
		}
		
		if (!player.isSneaking() || origin.distanceSquared(player.getLocation()) > distance * distance) {
			remove();
			return;
		} else {
			dash();
		}
		
	}
	
	public void dash() {
		Vector direction = player.getLocation().getDirection().normalize().multiply(2);
		player.setVelocity(direction);
		Methods.getSpiritType(bPlayer, player.getLocation(), 0.5F, 1, 0.5F, 0, 10);
		
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(player.getLocation(), 1)) {
			if (((target instanceof LivingEntity)) && (target.getEntityId() != player.getEntityId())) {
				Vector newDir = player.getLocation().getDirection().normalize().multiply(0);
				player.setVelocity(newDir);
				
				LivingEntity le = (LivingEntity)target;
				ParticleEffect.FIREWORKS_SPARK.display(target.getLocation(), 0.5F, 1, 0.5F, 1, 10);
				le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, strengthDuration * 100, 0), true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, weaknessDuration * 100, 0), true);
			}
		}
	}

	@Override
	public Object createNewComboInstance(Player player) {
		return new Fuse(player);
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		ArrayList<AbilityInformation> combination = new ArrayList<>();
		combination.add(new AbilityInformation("Soar", ClickType.LEFT_CLICK));
		combination.add(new AbilityInformation("Soar", ClickType.LEFT_CLICK));
		combination.add(new AbilityInformation("Possess", ClickType.SHIFT_DOWN));
		return combination;
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
		return "Fuse";
	}
	
	@Override
	public String getDescription() {
		return "Nothing yet";
	}
	
	@Override
	public String getInstructions() {
		return "Soar (Left-click 2x) > Possess (Hold shift)";
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
