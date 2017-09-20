package me.xnuminousx.spirits.Config;

import org.bukkit.configuration.file.FileConfiguration;

import com.projectkorra.projectkorra.configuration.ConfigManager;

import me.xnuminousx.spirits.Main;

public class AbilityConfig {
	
	static Main plugin;
	
	public AbilityConfig(Main plugin) {
		AbilityConfig.plugin = plugin;
		loadConfig();
	}
	
	private void loadConfig() {
		FileConfiguration config;
		config = Main.plugin.getConfig();
		
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.Avatar", "WHITE");
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.AvatarSub", "AQUA");
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.DarkAvatar", "DARK_RED");
		
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.Spirit", "DARK_AQUA");
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.SpiritSub", "DARK_PURPLE");
		ConfigManager.languageConfig.get().addDefault("Chat.Prefixes.Spirit", "[Spirit]");
		ConfigManager.languageConfig.get().addDefault("Chat.Prefixes.SpiritSub", "[Spirit]");
		
		config.addDefault("Abilities.Spirits.Dash.Enable", true);
		config.addDefault("Abilities.Spirits.Dash.Cooldown", 2000);
		config.addDefault("Abilities.Spirits.Dash.Distance", 3);
		
		config.addDefault("Abilities.Spirits.Possess.Enable", true);
		config.addDefault("Abilities.Spirits.Possess.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.Possess.Radius", 5);
		config.addDefault("Abilities.Spirits.Possess.Damage", 5);
		config.addDefault("Abilities.Spirits.Possess.Duration", 2000);
		
		config.addDefault("Abilities.Spirits.Soar.Enable", true);
		config.addDefault("Abilities.Spirits.Soar.Cooldown", 4500);
		config.addDefault("Abilities.Spirits.Soar.Duration", 1000);
		config.addDefault("Abilities.Spirits.Soar.Speed", 0.8);
		
		config.addDefault("Abilities.Spirits.Alleviate.Enable", true);
		config.addDefault("Abilities.Spirits.Alleviate.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.Alleviate.Radius", 5);
		config.addDefault("Abilities.Spirits.Alleviate.PotionInterval", 2000);
		config.addDefault("Abilities.Spirits.Alleviate.HealInterval", 5000);
		config.addDefault("Abilities.Spirits.Alleviate.ParticleColor (Has to be 6 characters)", "FFFFFF");
		
		config.addDefault("Abilities.Spirits.Sanctity.Cooldown", 6500);
		config.addDefault("Abilities.Spirits.Sanctity.Power", 3);
		config.addDefault("Abilities.Spirits.Sanctity.Duration", 6);
		config.addDefault("Abilities.Spirits.Sanctity.ChargeTime", 2500);
		
		config.addDefault("Abilities.Spirits.Intoxicate.Enable", true);
		config.addDefault("Abilities.Spirits.Intoxicate.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.Intoxicate.Radius", 5);
		config.addDefault("Abilities.Spirits.Intoxicate.PotionInterval", 2000);
		config.addDefault("Abilities.Spirits.Intoxicate.HarmInterval", 5000);
		config.addDefault("Abilities.Spirits.Intoxicate.ParticleColor (Has to be 6 characters)", "BD0000");
		
		config.addDefault("Abilities.Spirits.Shackle.Enable", true);
		config.addDefault("Abilities.Spirits.Shackle.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.Shackle.Duration", 2500);
		config.addDefault("Abilities.Spirits.Shackle.Range", 20);
		config.addDefault("Abilities.Spirits.Shackle.Radius", 2);
		
		config.options().copyDefaults();
		plugin.saveConfig();
	}
}
