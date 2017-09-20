package me.xnuminousx.spirits.ability.passive;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.PassiveAbility;

import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class Weightlessness extends SpiritAbility implements PassiveAbility {

	public Weightlessness(Player player) {
		super(player);
	}

	@Override
	public long getCooldown() {
		return 0;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "Weightlessness";
	}
	
	@Override
	public String getDescription() {
		return "Spirits are so light that not even gravity seems to recognize that they're there. Because of this, when they land on the ground they seem unharmed.";
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
	public void progress() {

	}

	@Override
	public boolean isInstantiable() {
		return false;
	}

}
