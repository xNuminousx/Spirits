package me.xnuminousx.spirits.ability.passive;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.PassiveAbility;

import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class SpiritResistance extends SpiritAbility implements PassiveAbility {
	
	public SpiritResistance(Player player) {
		super(player);
	}


	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, player.getLocation())) {
			return;
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 1), true);
	}

	@Override
	public long getCooldown() {
		return 0;
	}
	
	@Override
	public boolean isHiddenAbility() {
		return true;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "SpiritResistance";
	}
	
	@Override
	public String getDescription() {
		return "The outter most layer of a Spirits body is incredibly tangible. Allowing them to teleport into other bodies as well as through walls. Because of this, they're naturally able to resist most damaging attacks.";
	}

	@Override
	public boolean isExplosiveAbility() {
		return false;
	}

	@Override
	public boolean isHarmlessAbility() {
		return true;
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
	public boolean isInstantiable() {
		return true;
	}

}
