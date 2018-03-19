package me.xnuminousx.spirits;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class Methods {
	public static String getVersion() {
		return "Beta 1.0.9";
	}
	
	public static String getAuthor() {
		return "xNuminousx";
	}
	
	public static void spiritParticles(BendingPlayer bPlayer, Location loc, float X, float Y, float Z, float speed, int amount) {
		Element ls = Element.getElement("LightSpirit");
		Element ds = Element.getElement("DarkSpirit");
		Element s = Element.getElement("Spirit");
		if (bPlayer.hasElement(ls) && bPlayer.hasElement(ds)) {
			ParticleEffect.MAGIC_CRIT.display(loc, X, Y, Z, speed, amount);
		
		} else if (bPlayer.hasElement(ds)) {
			ParticleEffect.WITCH_MAGIC.display(loc, X, Y, Z, speed, amount);
			
		} else if (bPlayer.hasElement(ls)) {
			ParticleEffect.INSTANT_SPELL.display(loc, X, Y, Z, speed, amount);
			
		} else if (!bPlayer.hasElement(ls) && !bPlayer.hasElement(ds) && bPlayer.hasElement(s)) {
			ParticleEffect.MAGIC_CRIT.display(loc, X, Y, Z, speed, amount);
		}
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
	
	public static String spiritChatColor(String spiritType) {
		ChatColor chatColor = null;
		
		if (spiritType.equalsIgnoreCase("spirit") || spiritType.equalsIgnoreCase("neutral")) {
			chatColor = ChatColor.BLUE;
		} else if (spiritType.equalsIgnoreCase("lightspirit") || spiritType.equalsIgnoreCase("light")) {
			chatColor = ChatColor.AQUA;
		} else if (spiritType.equalsIgnoreCase("darkspirit") || spiritType.equalsIgnoreCase("dark")) {
			chatColor = ChatColor.DARK_GRAY;
		}
		
		return chatColor + "";
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
	
	public static void createPolygon(Location location, int points, int radius, double height, ParticleEffect particleEffect) {
		for (int i = 0; i < points; i++) {
			  double angle = 360.0 / points * i;
			  angle = Math.toRadians(angle);
			  double x = radius * Math.cos(angle);
			  double z = radius * Math.sin(angle);
			  location.add(x, height, z);
			  particleEffect.display(location, 0, 0, 0, 0, 1);
			  location.subtract(x, height, z);
			}
	}
	
	public static void createRotatingCircle(Location location, int speed, int points, int radius, double height, ParticleEffect particleEffect) {
		for (int i = 0; i < speed; i++) {
			int currPoint = 0;
			currPoint += 360 / points;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle = currPoint * Math.PI / 180.0D;
			double x = radius * Math.cos(angle);
			double z = radius * Math.sin(angle);
			location.add(x, height, z);
			particleEffect.display(location, 0, 0, 0, 0, 1);
			location.subtract(x, height, z);
		}
	}
	
	public static void createDoubleRotatingCircle(Location location, Location location2, int speed, int points, int radius, double height, ParticleEffect particleEffect1, ParticleEffect particleEffect2, int particleAmount) {
		for (int i = 0; i < speed; i++) {
			int currPoint = 0;
			currPoint += 360 / points;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle = currPoint * Math.PI / 180.0D;
			double x = radius * Math.cos(angle);
			double x2 = radius * Math.sin(angle);
			double z = radius * Math.sin(angle);
			double z2 = radius * Math.cos(angle);
			location.add(x, height, z);
			particleEffect1.display(location, 0, 0, 0, 0, particleAmount);
			location.subtract(x, height, z);
			
			location2.add(x2, height, z2);
			particleEffect2.display(location2, 0, 0, 0, 0, particleAmount);
			location2.subtract(x2, height, z2);
		}
	}
}
