package me.xnuminousx.spirits;

import java.util.logging.*;

import org.bukkit.plugin.java.JavaPlugin;
import com.projectkorra.projectkorra.ability.CoreAbility;

import me.xnuminousx.spirits.Config.AbilityConfig;
import me.xnuminousx.spirits.Listeners.AbilityListener;

public class Main extends JavaPlugin {

	public static Main plugin;
	public static Logger log;
	public static String dev;
	public static String version;
	
	@Override
	public void onEnable() {
		plugin = this;
		new AbilityConfig(this);
		
		dev = this.getDescription().getAuthors().toString().replace("[", "").replace("]", "");
		version = this.getDescription().getVersion();

		CoreAbility.registerPluginAbilities(plugin, "me.xnuminousx.spirits.ability");
		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
	}
	
	public void onDisable() {
		
	}
}
