package me.xnuminousx.spirits.config;

import org.bukkit.configuration.file.FileConfiguration;

import com.projectkorra.projectkorra.configuration.ConfigManager;

import me.xnuminousx.spirits.Main;

public class AbilityConfig {
	
	public AbilityConfig(Main plugin) {
		FileConfiguration config = plugin.getConfig();
		config.options().copyDefaults(true);
		
		ConfigManager.languageConfig.get().addDefault("Chat.Prefixes.Spirit", "[Spirit]");
		
		config.addDefault("Chat.Colors.Avatar", "WHITE");
		config.addDefault("Chat.Colors.AvatarSub", "AQUA");
		config.addDefault("Chat.Colors.DarkAvatar", "DARK_RED");
		config.addDefault("Chat.Colors.Spirit", "DARK_AQUA");
		config.addDefault("Chat.Colors.SpiritSub", "DARK_PURPLE");
		
		config.addDefault("Storage.engine", "sqlite");
		config.addDefault("Storage.MySQL.host", "localhost");
		config.addDefault("Storage.MySQL.port", 3306);
		config.addDefault("Storage.MySQL.pass", "");
		config.addDefault("Storage.MySQL.db", "minecraft");
		config.addDefault("Storage.MySQL.user", "root");
		
		config.addDefault("Abilities.Spirit.Dash.Enable", true);
		config.addDefault("Abilities.Spirit.Dash.Cooldown", 2000);
		config.addDefault("Abilities.Spirit.Dash.Distance", 3);
		
		config.addDefault("Abilities.Spirit.Possess.Enable", true);
		config.addDefault("Abilities.Spirit.Possess.Cooldown", 5000);
		config.addDefault("Abilities.Spirit.Possess.Radius", 5);
		config.addDefault("Abilities.Spirit.Possess.Damage", 5);
		config.addDefault("Abilities.Spirit.Possess.Duration", 2000);
		
		config.addDefault("Abilities.Spirit.Soar.Enable", true);
		config.addDefault("Abilities.Spirit.Soar.Cooldown", 4500);
		config.addDefault("Abilities.Spirit.Soar.Duration", 1000);
		config.addDefault("Abilities.Spirit.Soar.Speed", 0.8);
		
		config.addDefault("Abilities.LightSpirit.Alleviate.Enable", true);
		config.addDefault("Abilities.LightSpirit.Alleviate.Cooldown", 5000);
		config.addDefault("Abilities.LightSpirit.Alleviate.Radius", 5);
		config.addDefault("Abilities.LightSpirit.Alleviate.PotionInterval", 2000);
		config.addDefault("Abilities.LightSpirit.Alleviate.HealInterval", 5000);
		config.addDefault("Abilities.LightSpirit.Alleviate.ParticleColor (Has to be 6 characters)", "FFFFFF");
		
		config.addDefault("Abilities.LightSpirit.Sanctity.Cooldown", 6500);
		config.addDefault("Abilities.LightSpirit.Sanctity.Power", 3);
		config.addDefault("Abilities.LightSpirit.Sanctity.Duration", 6);
		config.addDefault("Abilities.LightSpirit.Sanctity.ChargeTime", 2500);
		
		config.addDefault("Abilities.DarkSpirit.Intoxicate.Enable", true);
		config.addDefault("Abilities.DarkSpirit.Intoxicate.Cooldown", 5000);
		config.addDefault("Abilities.DarkSpirit.Intoxicate.Radius", 5);
		config.addDefault("Abilities.DarkSpirit.Intoxicate.PotionInterval", 2000);
		config.addDefault("Abilities.DarkSpirit.Intoxicate.HarmInterval", 5000);
		config.addDefault("Abilities.DarkSpirit.Intoxicate.ParticleColor (Has to be 6 characters)", "BD0000");
		
		config.addDefault("Abilities.DarkSpirit.Shackle.Enable", true);
		config.addDefault("Abilities.DarkSpirit.Shackle.Cooldown", 5000);
		config.addDefault("Abilities.DarkSpirit.Shackle.Duration", 2500);
		config.addDefault("Abilities.DarkSpirit.Shackle.Range", 20);
		config.addDefault("Abilities.DarkSpirit.Shackle.Radius", 2);
		
		ConfigManager.defaultConfig.save();
		plugin.saveConfig();
	}
}
