package me.xnuminousx.spirits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.projectkorra.projectkorra.ability.CoreAbility;

import me.xnuminousx.spirits.config.AbilityConfig;
import me.xnuminousx.spirits.listeners.AbilityListener;
import me.xnuminousx.spirits.listeners.PlayerListener;
import me.xnuminousx.spirits.misc.DBConnection;
import me.xnuminousx.spirits.misc.PlayerData;

public class Main extends JavaPlugin {

	public static Main plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		new AbilityConfig(this);
		
		DBConnection.host = getConfig().getString("Storage.MySQL.host");
		DBConnection.port = getConfig().getInt("Storage.MySQL.port");
		DBConnection.pass = getConfig().getString("Storage.MySQL.pass");
		DBConnection.db = getConfig().getString("Storage.MySQL.db");
		DBConnection.user = getConfig().getString("Storage.MySQL.user");
		DBConnection.init();
		if (DBConnection.isOpen() == false) {
			return;
		}
		
		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerData.createPlayerData(player.getUniqueId());
		}
		
		CoreAbility.registerPluginAbilities(plugin, "me.xnuminousx.spirits.ability");
	}
	
	public void onDisable() {
		if (DBConnection.isOpen != false) {
			DBConnection.sql.close();
		}
	}
}
