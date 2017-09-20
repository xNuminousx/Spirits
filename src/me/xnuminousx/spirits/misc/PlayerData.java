package me.xnuminousx.spirits.misc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ProjectKorra;

import me.xnuminousx.spirits.elements.SpiritElement;
import me.xnuminousx.spirits.misc.DBConnection;
import me.xnuminousx.spirits.Main;

public class PlayerData {

	private static final Map<UUID, PlayerData> PLAYER_DATA = new HashMap<>();

	private UUID uuid;
	private SubElement spirit;

	public PlayerData(UUID uuid) {
		this(uuid, null);
	}

	public PlayerData(UUID uuid, SubElement spirit) {
		this.uuid = uuid;
		this.spirit = spirit;

		if (spirit != null) {
			if (spirit == SpiritElement.LIGHT_SPIRIT) {
				setLightSpirit();
			} else if (spirit == SpiritElement.DARK_SPIRIT) {
				setDarkSpirit();
			}
		}
		Player player = Main.plugin.getServer().getPlayer(uuid);
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		if (bPlayer != null) {
			if (bPlayer.hasSubElement(SpiritElement.LIGHT_SPIRIT) && bPlayer.hasSubElement(SpiritElement.DARK_SPIRIT)) {
				SubElement spiritType = Math.random() > 0.5 ? SpiritElement.LIGHT_SPIRIT : SpiritElement.DARK_SPIRIT;
				Main.plugin.getLogger().warning("Player " + player.getName() + " has permission for dark and light spirit. Check your permissions configuration to remove their access for 1 of these subelements.");
				Main.plugin.getLogger().info("Default falling back to " + spiritType.getName() + " for " + player.getName());

				bPlayer.getSubElements().remove(SpiritElement.DARK_SPIRIT);
				bPlayer.getSubElements().remove(SpiritElement.LIGHT_SPIRIT);
				bPlayer.addSubElement(spiritType);
			}
		}

		PLAYER_DATA.put(uuid, this);
	}

	/**
	 * Returns the UUID of the {@link PlayerData}.
	 * 
	 * @return uuid
	 */
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * Check to see if a player is a spirit
	 * 
	 * @return true if the player is a spirit
	 */
	public boolean isSpirit() {
		return spirit != null;
	}

	/**
	 * Check to see if a player is a light spirit
	 * 
	 * @return true if the player is a light spirit
	 */
	public boolean isLightSpirit() {
		return !isSpirit() ? false : spirit.equals(SpiritElement.LIGHT_SPIRIT);
	}

	/**
	 * Check to see if a player is a dark spirit
	 * 
	 * @return true if the player is a dark spirit
	 */
	public boolean isDarkSpirit() {
		return !isSpirit() ? false : spirit.equals(SpiritElement.DARK_SPIRIT);
	}

	/**
	 * Set the player as a light spirit
	 */
	public void setLightSpirit() {
		if (!isLightSpirit()) {
			spirit = SpiritElement.LIGHT_SPIRIT;
			DBConnection.sql.modifyQuery("UPDATE spirit_data SET spirit = 'l' WHERE uuid = '" + uuid + "'");
		}
	}

	/**
	 * Set the player as a dark spirit
	 */
	public void setDarkSpirit() {
		if (!isDarkSpirit()) {
			spirit = SpiritElement.DARK_SPIRIT;
			DBConnection.sql.modifyQuery("UPDATE spirit_data SET spirit = 'd' WHERE uuid = '" + uuid + "'");
		}
	}

	/**
	 * Get the player's spirit type
	 * 
	 * @return spirit type
	 */
	public SubElement getSpiritType() {
		return spirit;
	}

	/**
	 * Remove the player's spirit
	 */
	public void removeSpirit() {
		spirit = null;
		DBConnection.sql.modifyQuery("UPDATE spirit_data SET spirit = NULL WHERE uuid = '" + uuid + "'");
	}

	/**
	 * Gets the PlayerData object for the specified UUID
	 * 
	 * @param uuid
	 * @return PlayerData
	 */
	public static PlayerData getPlayerData(UUID uuid) {
		return PLAYER_DATA.containsKey(uuid) ? PLAYER_DATA.get(uuid) : null;
	}

	/**
	 * Gets the PlayerData object for the specified Player
	 * 
	 * @param player
	 * @return PlayerData
	 */
	public static PlayerData getPlayerData(Player player) {
		return getPlayerData(player.getUniqueId());
	}

	/**
	 * Create a new PlayerData object based on properties stored in the
	 * database. If it is a new user, then create a new row in the database for
	 * this user.
	 * 
	 * @param uuid
	 * @return true if successful
	 */
	public static void createPlayerData(UUID uuid) {
		new BukkitRunnable() {
			@Override
			public void run() {
				createPlayerDataAsync(uuid);
			}
		}.runTaskAsynchronously(ProjectKorra.plugin);
	}

	public static void createPlayerDataAsync(UUID uuid) {
		ResultSet rs = DBConnection.sql.readQuery("SELECT * FROM spirit_data WHERE uuid = '" + uuid.toString() + "'");
		try {
			if (!rs.next()) {
				new PlayerData(uuid);
				DBConnection.sql.modifyQuery("INSERT INTO spirit_data (uuid) VALUES ('" + uuid.toString() + "')");
			} else {
				String spiritType = rs.getString("spirit");
				SubElement spirit = null;
				if (spiritType != null) {
					if (spiritType.equalsIgnoreCase("l")) {
						spirit = SpiritElement.LIGHT_SPIRIT;
					} else if (spiritType.equalsIgnoreCase("d")) {
						spirit = SpiritElement.DARK_SPIRIT;
					}
				}
				SubElement _spirit = spirit;
				new BukkitRunnable() {
					@Override
					public void run() {
						new PlayerData(uuid, _spirit);
					}
				}.runTask(ProjectKorra.plugin);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

}