package me.xnuminousx.spirits;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class Methods {
	
	/*
	 * The types of spirits in the plugin.
	 */
	public enum SpiritType {
		DARK, LIGHT, NEUTRAL
	}
	
	/*
	 * Used to create some polygon in a specific location.
	 * 
	 * location = The center of the polygon.
	 * points = The type of polygon it will be.
	 * radius = How far from the location of the polygon the edge is.
	 * height = How high from the location the polygon spawns.
	 * particleEffect = What type of particle spawns.
	 */
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
	
	/*
	 * Used to create a circle which rotates at a certain
	 * speed around a certain location.
	 * 
	 * location = The center of the circle.
	 * speed = The speed at which the circle progresses.
	 * points = The amount of points which spawn particles.
	 * radius = How far from the location of the circle the edge is.
	 * height = How high from the location the circle spawns.
	 * particleEffect = What type of particle spawns.
	 */
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

	/*
	 * The author(s) of the project.
	 */
	public static String getAuthor() {
		return "Numin";
	}
	
	/*
	 * The current version of Spirits
	 */
	public static String getVersion() {
		return "Beta 1.0.11";
	}
	
	/* 
	 * Used to get the spirit type of a specific player. This allows
	 * certain actions to be ran depending on their spirit type.
	 * 
	 * player = The player which is being checked for spirit elements.
	 */
	public static SpiritType getSpiritType(Player player) {
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		Element ls = Element.getElement("LightSpirit");
		Element ds = Element.getElement("DarkSpirit");
		Element s = Element.getElement("Spirit");
		
		if ((bPlayer.hasElement(ls) && bPlayer.hasElement(ds)) || (!bPlayer.hasElement(ls) && !bPlayer.hasElement(ds)) || (bPlayer.hasElement(s))) {
			return SpiritType.NEUTRAL;
		} else if (bPlayer.hasElement(ls)) {
			return SpiritType.LIGHT;
		} else if (bPlayer.hasElement(ds)) {
			return SpiritType.DARK;
		} else {
			return null;
		}
	}
	
	/*
	 * Used to play particles which attach to the info of a player so that
	 * the particles change type depending on what type of spirit is using them.
	 * 
	 * bPlayer = The BendingPlayer which the particles will attach to.
	 * location = The location at which to display the particles.
	 * X / Y / Z = The distance in each respective coordinate the particles are allowed to get.
	 * speed = The speed at which the particles move.
	 * amount = The amount of particles that spawn per tick.
	 */
	public static void playSpiritParticles(BendingPlayer bPlayer, Location location, float X, float Y, float Z, float speed, int amount) {
		Element ls = Element.getElement("LightSpirit");
		Element ds = Element.getElement("DarkSpirit");
		Element s = Element.getElement("Spirit");
		
		if (bPlayer.hasElement(ls) && bPlayer.hasElement(ds)) {
			ParticleEffect.MAGIC_CRIT.display(location, X, Y, Z, speed, amount);
		
		} else if (!bPlayer.hasElement(ls) && !bPlayer.hasElement(ds) && bPlayer.hasElement(s)) {
			ParticleEffect.MAGIC_CRIT.display(location, X, Y, Z, speed, amount);
			
		} else if (bPlayer.hasElement(ds)) {
			ParticleEffect.WITCH_MAGIC.display(location, X, Y, Z, speed, amount);
			
		} else if (bPlayer.hasElement(ls)) {
			ParticleEffect.INSTANT_SPELL.display(location, X, Y, Z, speed, amount);
			
		}
	}
	
	/*
	 * Used to play spirit particles without the need of a player.
	 * 
	 * spiritType = The type of spirit particles to display.
	 * location = the location at which to display the particles.
	 * X / Y / Z = The distance in each respective coordinate the particles are allowed to get.
	 * speed = The speed at which the particles move.
	 * amount = The amount of particles that spawn per tick.
	 */
	public static void playSpiritParticles(SpiritType spiritType, Location location, float X, float Y, float Z, float speed, int amount) {
		
		if (spiritType == SpiritType.NEUTRAL) {
			ParticleEffect.MAGIC_CRIT.display(location, X, Y, Z, speed, amount);
		
		} else if (spiritType == SpiritType.DARK) {
			ParticleEffect.WITCH_MAGIC.display(location, X, Y, Z, speed, amount);
			
		} else if (spiritType == SpiritType.LIGHT) {
			ParticleEffect.INSTANT_SPELL.display(location, X, Y, Z, speed, amount);
			
		}
	}
	
	/*
	 * Used to set the velocity of a player.
	 * 
	 * player = The player which is being manipulated.
	 * isForward = Whether the velocity is going forward or backwards.
	 * speed = The speed at which the player is being sent.
	 * height = The height from their original location the player is shot.
	 */
	public static void setPlayerVelocity(Player player, boolean isForward, float speed, double height) {
		Location location = player.getLocation();
		Vector direction;
		if (isForward) {
			direction = location.getDirection().normalize().multiply(speed);
		} else {
			direction = location.getDirection().normalize().multiply(-speed);
		}
		direction.setY(height);
		player.setVelocity(direction);
	}
	
	/*
	 * Used to set the color of a string of text in chat.
	 * 
	 * spiritType = The type of spirit color that will appear.
	 */
	public static String setSpiritDescriptionColor(SpiritType spiritType) {
		ChatColor chatColor = null;
		
		if (spiritType == SpiritType.NEUTRAL) {
			chatColor = ChatColor.BLUE;
		} else if (spiritType == SpiritType.LIGHT) {
			chatColor = ChatColor.AQUA;
		} else if (spiritType == SpiritType.DARK) {
			chatColor = ChatColor.DARK_GRAY;
		}
		
		return chatColor + "";
	}

	/*
	 * Used to set a unique spirit description in chat
	 * which will appear in the '/b h' command.
	 * 
	 * spiritType = The type of spirit color that will appear.
	 * abilityType = What type of ability the description is for.
	 */
	public static String setSpiritDescription(SpiritType spiritType, String abilityType) {
		ChatColor titleColor = null;
		ChatColor descColor = null;
		
		if (spiritType == SpiritType.NEUTRAL) {
			titleColor = ChatColor.BLUE;
			descColor = ChatColor.DARK_AQUA;
		} else if (spiritType == SpiritType.LIGHT) {
			titleColor = ChatColor.AQUA;
			descColor = ChatColor.WHITE;
		} else if (spiritType == SpiritType.DARK) {
			titleColor = ChatColor.DARK_GRAY;
			descColor = ChatColor.DARK_RED;
		}
		
		return titleColor + "" + ChatColor.BOLD + abilityType + ": " + descColor;
	}
}
