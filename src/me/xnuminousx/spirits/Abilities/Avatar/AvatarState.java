package me.xnuminousx.spirits.Abilities.Avatar;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AvatarAbility;

import net.md_5.bungee.api.ChatColor;

public class AvatarState extends AvatarAbility implements AddonAbility {

	public AvatarState(Player player) {
		super(player);
	}

	@Override
	public void progress() {

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
		return "AvatarState";
	}
	
	@Override
	public String getDescription() {
		return "The AvatarState is the most powerful ability of the Avatar. It is a defense mechanism designed to give you eminse power. When in the AvatarState, all of your bending abilities will be empowered for the duration of your AvatarState. You will also be giving strength and resistance as the AvatarState allows you to take damage with ease.";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.AQUA + "Nothing yet";
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
	public boolean isHarmlessAbility() {
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
