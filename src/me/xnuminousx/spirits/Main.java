package me.xnuminousx.spirits;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.projectkorra.ability.CoreAbility;

import me.xnuminousx.spirits.Config.AbilityConfig;
import me.xnuminousx.spirits.Listeners.AbilityListener;

public class Main extends JavaPlugin {
	public static Main plugin;
	public static Logger log;
	public static String development;
	public static String version;
	
	@Override
	public void onEnable() {
		plugin = this;
		Main.log = this.getLogger();
		new AbilityConfig(this);
		
		CoreAbility.registerPluginAbilities(this, "me.xnuminousx.spirits.ability");
		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
		
		development = this.getDescription().getAuthors().toString().replaceAll("[", "").replace("]", "");
		version = this.getDescription().getVersion();
	}
	
	@Override public void onDisable() {
		
	}
}
