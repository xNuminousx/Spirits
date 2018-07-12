package me.xnuminousx.spirits;

import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.projectkorra.ability.CoreAbility;

import me.xnuminousx.spirits.config.Config;
import me.xnuminousx.spirits.elements.SpiritElement;
import me.xnuminousx.spirits.listeners.AbilityListener;
import me.xnuminousx.spirits.listeners.PassiveListener;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		new Config(this);
		new SpiritElement();
		
		CoreAbility.registerPluginAbilities(plugin, "me.xnuminousx.spirits.ability");
		registerListeners();
		
		plugin.getLogger().info("Successfully enabled Spirits " + Methods.getVersion());
	}
	
	@Override
	public void onDisable() {
		
		plugin.getLogger().info("Successfully disabled Spirits " + Methods.getVersion());
	}
	
	public static Main getInstance() {
		return plugin;
	}
	
	public void registerListeners() {
		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
		getServer().getPluginManager().registerEvents(new PassiveListener(), this);
	}

}
