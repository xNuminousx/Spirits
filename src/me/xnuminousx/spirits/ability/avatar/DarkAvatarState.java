package me.xnuminousx.spirits.ability.avatar;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;

import me.xnuminousx.spirits.ability.api.DarkAvatarAbility;
import net.md_5.bungee.api.ChatColor;

public class DarkAvatarState extends DarkAvatarAbility implements AddonAbility {

	public DarkAvatarState(Player player) {
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
		return "DarkAvatarState";
	}
	
	@Override
	public String getDescription() {
		return ChatColor.DARK_PURPLE + "Version of AvatarState but for the Dark Avatar";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.DARK_PURPLE + "Nothing yet";
	}

	@Override
	public String getAuthor() {
		return ChatColor.DARK_PURPLE + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return ChatColor.DARK_PURPLE + "1.0";
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
