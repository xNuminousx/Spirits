package me.numin.spirits.utilities;

import java.util.List;

import me.numin.spirits.Spirits;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;

public class Methods {

    /**
     * The types of Spirits in the plugin.
     */
    public enum SpiritType {
        DARK, LIGHT, NEUTRAL
    }

    /**
     * Used to move a location in a certain direction.
     *
     * @param direction The direction in which to move.
     * @param point The starting point from which the location moves.
     * @param speed The speed of the point.
     * @return The new location.
     */
    public static Location advanceLocationToDirection(Vector direction, Location point, double speed) {
        point.add(direction.multiply(speed).normalize().clone());
        return point;
    }

    /**
     * Moves a location from one point to another.
     *
     * @param vector The vector used to move the points.
     * @param from The starting point.
     * @param to The end point.
     * @param speed The speed of the point.
     * @return The new location.
     */
    public static Location advanceLocationToPoint(Vector vector, Location from, Location to, double speed) {
        vector.add(to.toVector()).subtract(from.toVector()).multiply(speed).normalize();
        from.add(vector.clone().multiply(speed));
        return from;
    }

    /**
     * A general animation to display when making a
     * Spirit/Player invisible.
     *
     * @param player The player being vanished.
     */
    public static void animateVanish(Player player) {
        Location location = player.getLocation().add(0, 1, 0);
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 20, 0, 0, 0, 0.09);
        player.getWorld().spawnParticle(Particle.PORTAL, location, 30, 0, 0, 0, 2);
        playSpiritParticles(player, location, 0.5, 0.5, 0.5, 0, 5);
    }

    /**
     * Used to create an inventory item.
     *
     * @param icon The item which displays in a players inventory.
     * @param name The name of the item.
     * @param color The color that the name displays in.
     * @return The item.
     */
    public static ItemStack createItem(Material icon, String name, ChatColor color) {
        ItemStack item = new ItemStack(icon);
        ItemMeta itemMeta = item.getItemMeta();

        assert itemMeta != null;
        itemMeta.setDisplayName(color + name);
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Used to create an inventory item with a lore.
     *
     * @param icon The item which displays in a players inventory.
     * @param name The name of the item.
     * @param color The color that the name displays in.
     * @param description The lore of the item.
     * @return The item.
     */
    public static ItemStack createItem(Material icon, String name, ChatColor color, List<String> description) {
        ItemStack item = createItem(icon, name, color);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Used to create some polygon at a location.
     *
     * @param location Center point for the polygon.
     * @param points How many vertices the polygon will have.
     * @param radius The radius the polygon will have.
     * @param height The height from location.
     * @param particle The type of particle that spawns at each point.
     */
    public static void createPolygon(Location location, int points, double radius, double height, Particle particle) {
        for (int i = 0; i < points; i++) {
            double angle = 360.0 / points * i;
            angle = Math.toRadians(angle);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            location.add(x, height, z);
            location.getWorld().spawnParticle(particle, location, 1, 0, 0, 0, 0);
            location.subtract(x, height, z);
        }
    }

    /**
     * Used to get the authors defined in the pom.xml file.
     *
     * @return List of authors.
     */
    public static List<String> getAuthor() {
        return Spirits.plugin.getDescription().getAuthors();
    }

    /**
     * Used to get the version of the project from the pom.xml file.
     *
     * @return The plugin version.
     */
    public static String getVersion() {
        return Spirits.plugin.getDescription().getVersion();
    }

    /**
     * Gets the type of spirit a player may be.
     *
     * @param player The player being tested.
     * @return The type of spirit they are, null if they aren't a spirit.
     */
    public static SpiritType getSpiritType(Player player) {
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        Element spirit = Element.getElement("Spirit"),
                lightSpirit = Element.getElement("LightSpirit"),
                darkSpirit = Element.getElement("DarkSpirit");

        if (bPlayer == null) return null;

        if (bPlayer.hasElement(lightSpirit) && bPlayer.hasElement(darkSpirit)) {
            return SpiritType.NEUTRAL;
        } else if (bPlayer.hasElement(lightSpirit)) {
            return SpiritType.LIGHT;
        } else if (bPlayer.hasElement(darkSpirit)) {
            return SpiritType.DARK;
        } else if (bPlayer.hasElement(spirit)) {
            return SpiritType.NEUTRAL;
        } else {
            return null;
        }
    }

    /**
     * Plays particles based on SpiritType.
     *
     * @param spiritType The type of particles to display.
     * @param location The location where the particles will spawn.
     * @param x The off-set of the X axis.
     * @param y The off-set of the Y axis.
     * @param z The off-set of the Z axis.
     * @param speed The particle speed.
     * @param amount The amount of particles to display.
     */
    public static void playSpiritParticles(SpiritType spiritType, Location location, double x, double y, double z, double speed, int amount) {
        DustOptions teal = new DustOptions(Color.fromRGB(0, 176, 180), 1),
                white = new DustOptions(Color.fromRGB(255, 255,255), 1),
                black = new DustOptions(Color.fromRGB(0, 0, 0), 1);

        if (spiritType == SpiritType.NEUTRAL) {
            location.getWorld().spawnParticle(Particle.CRIT_MAGIC, location, amount, x, y, z, speed);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, amount, x, y, z, speed, teal);
        } else if (spiritType == SpiritType.DARK) {
            location.getWorld().spawnParticle(Particle.SPELL_WITCH, location, amount, x, y, z, speed);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, amount, x, y, z, speed, black);

        } else if (spiritType == SpiritType.LIGHT) {
            location.getWorld().spawnParticle(Particle.SPELL_INSTANT, location, amount, x, y, z, speed);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, amount, x, y, z, speed, white);
        }
    }

    /**
     * Used to play particles based on the type of Spirit a
     * player is.
     *
     * @param player The player being tested.
     * @param location The location of the particles.
     * @param x The off-set of the X axis.
     * @param y The off-set of the Y axis.
     * @param z The off-set of the Z axis.
     * @param speed The particle speed.
     * @param amount The amount of particles to display.
     */
    public static void playSpiritParticles(Player player, Location location, double x, double y, double z, double speed, int amount) {
        SpiritType spiritType = getSpiritType(player);
        if (spiritType == null) return;
        playSpiritParticles(spiritType, location, x, y, z, speed, amount);
    }

    /**
     * Sets the velocity of a player.
     *
     * @param target The target being manipulated.
     * @param speed The speed of the player.
     * @return The players new velocity.
     */
    public static Vector setVelocity(LivingEntity target, float speed) {
        Location location = target.getLocation();
        return location.getDirection().normalize().multiply(speed);
    }

    /**
     * Sets the velocity of a player with a height
     * addition.
     *
     * @param target The target being manipulated.
     * @param speed The speed of the player.
     * @param height The height of the player.
     * @return The players new velocity.
     */
    public static Vector setVelocity(LivingEntity target, float speed, double height) {
        Vector direction = setVelocity(target, speed);
        direction.setY(height);
        return direction;
    }

    /**
     * Used to get a ChatColor based on SpiritType.
     *
     * @param spiritType The SpiritType being tested.
     * @return The correct chat color.
     */
    public static ChatColor getSpiritColor(SpiritType spiritType) {
        switch (spiritType) {
            case NEUTRAL: return ChatColor.BLUE;
            case LIGHT: return ChatColor.AQUA;
            case DARK: return ChatColor.DARK_GRAY;
            default: return null;
        }
    }

    /**
     * Used to create a string with the generic Spirits
     * formatting.
     *
     * @param spiritType The type of Spirit colors to use.
     * @param abilityType The header for the description.
     * @return The new description.
     */
    public static String setSpiritDescription(SpiritType spiritType, String abilityType) {
        ChatColor titleColor = getSpiritColor(spiritType);
        ChatColor descColor = null;

        switch (spiritType) {
            case NEUTRAL: descColor = ChatColor.DARK_AQUA;
            break;
            case LIGHT: descColor = ChatColor.WHITE;
            break;
            case DARK: descColor = ChatColor.DARK_RED;
            break;
        }
        return titleColor + "" + ChatColor.BOLD + abilityType + ": " + descColor;
    }
}