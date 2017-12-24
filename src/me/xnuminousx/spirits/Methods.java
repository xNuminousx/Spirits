package me.xnuminousx.spirits;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;

public class Methods {
	public static void spiritParticles(BendingPlayer bPlayer, Location loc, float X, float Y, float Z, float speed, int amount) {
		Element ls = Element.getElement("LightSpirit");
		Element ds = Element.getElement("DarkSpirit");
		Element s = Element.getElement("Spirit");
		if (bPlayer.hasElement(ls) && bPlayer.hasElement(ds)) {
			GeneralMethods.displayColoredParticle(loc, "FF0000", X, Y, Z);
			GeneralMethods.displayColoredParticle(loc, "00A315", X, Y, Z);
		
		} else if (bPlayer.hasElement(ds)) {
			GeneralMethods.displayColoredParticle(loc, "FF0000", X, Y, Z);
			GeneralMethods.displayColoredParticle(loc, "00A315", X, Y, Z);
			
		} else if (bPlayer.hasElement(ls)) {
			GeneralMethods.displayColoredParticle(loc, "FF0000", X, Y, Z);
			GeneralMethods.displayColoredParticle(loc, "00A315", X, Y, Z);
			
		} else if (!bPlayer.hasElement(ls) && !bPlayer.hasElement(ds) && bPlayer.hasElement(s)) {
			GeneralMethods.displayColoredParticle(loc, "FF0000", X, Y, Z);
			GeneralMethods.displayColoredParticle(loc, "00A315", X, Y, Z);
		}
		return;
	}
	
	public static String getSpiritDescription(String spiritType, String abilityType) {
		ChatColor titleColor = null;
		ChatColor descColor = null;
		
		if (spiritType.equalsIgnoreCase("spirit") || spiritType.equalsIgnoreCase("neutral")) {
			titleColor = ChatColor.BLUE;
			descColor = ChatColor.DARK_AQUA;
		} else if (spiritType.equalsIgnoreCase("lightspirit") || spiritType.equalsIgnoreCase("light")) {
			titleColor = ChatColor.AQUA;
			descColor = ChatColor.WHITE;
		} else if (spiritType.equalsIgnoreCase("darkspirit") || spiritType.equalsIgnoreCase("dark")) {
			titleColor = ChatColor.DARK_GRAY;
			descColor = ChatColor.DARK_RED;
		}
		
		return titleColor + "" + ChatColor.BOLD + abilityType + ": " + descColor;
	}
	public static void setPlayerVelocity(Player player, Location location, boolean isForward, float speed, double height) {
		location = player.getLocation();
		Vector direction;
		if (isForward) {
			direction = location.getDirection().normalize().multiply(speed);
		} else {
			direction = location.getDirection().normalize().multiply(-speed);
		}
		direction.setY(height);
		player.setVelocity(direction);
	}
}
