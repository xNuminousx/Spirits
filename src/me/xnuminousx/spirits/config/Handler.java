package me.xnuminousx.spirits.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AvatarAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

import me.xnuminousx.spirits.listeners.AbilityListener;

public class Handler extends AvatarAbility implements AddonAbility {

	public Handler(Player player) {
		super(player);
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new AbilityListener(), ProjectKorra.plugin);
		
		//Element console load message
		ProjectKorra.log.info("Successfully loaded Spirits");
		
		FileConfiguration langConfig = ConfigManager.languageConfig.get();
	
		langConfig.addDefault("Chat.Colors.Spirit", "DARK_AQUA");
		langConfig.addDefault("Chat.Colors.LightSpirit", "AQUA");
		langConfig.addDefault("Chat.Colors.DarkSpirit", "BLUE");
		langConfig.addDefault("Chat.Prefixes.Spirit", "[Spirit]");
		langConfig.addDefault("Chat.Prefixes.LightSpirit", "[LightSpirit]");
		langConfig.addDefault("Chat.Prefixes.DarkSpirit", "[DarkSpirit]");
		
		FileConfiguration config = ConfigManager.getConfig();
		
		config.addDefault("ExtraAbilities.Spirit.Dash.Enable", true);
		config.addDefault("ExtraAbilities.Spirit.Dash.Cooldown", 2000);
		config.addDefault("ExtraAbilities.Spirit.Dash.Distance", 3);
		
		config.addDefault("ExtraAbilities.Spirit.Possess.Enable", true);
		config.addDefault("ExtraAbilities.Spirit.Possess.Cooldown", 5000);
		config.addDefault("ExtraAbilities.Spirit.Possess.Radius", 5);
		config.addDefault("ExtraAbilities.Spirit.Possess.Damage", 5);
		config.addDefault("ExtraAbilities.Spirit.Possess.Duration", 2000);
		
		config.addDefault("ExtraAbilities.Spirit.Soar.Enable", true);
		config.addDefault("ExtraAbilities.Spirit.Soar.Cooldown", 4500);
		config.addDefault("ExtraAbilities.Spirit.Soar.Duration", 1000);
		config.addDefault("ExtraAbilities.Spirit.Soar.Speed", 0.8);
		
		config.addDefault("ExtraAbilities.LightSpirit.Alleviate.Enable", true);
		config.addDefault("ExtraAbilities.LightSpirit.Alleviate.Cooldown", 5000);
		config.addDefault("ExtraAbilities.LightSpirit.Alleviate.Radius", 5);
		config.addDefault("ExtraAbilities.LightSpirit.Alleviate.PotionInterval", 2000);
		config.addDefault("ExtraAbilities.LightSpirit.Alleviate.HealInterval", 5000);
		config.addDefault("ExtraAbilities.LightSpirit.Alleviate.ParticleColor (Has to be 6 characters)", "FFFFFF");
		
		config.addDefault("ExtraAbilities.LightSpirit.Sanctity.Enable", true);
		config.addDefault("ExtraAbilities.LightSpirit.Sanctity.Cooldown", 15000);
		config.addDefault("ExtraAbilities.LightSpirit.Sanctity.Power", 2);
		config.addDefault("ExtraAbilities.LightSpirit.Sanctity.PotionDuration", 1.5);
		config.addDefault("ExtraAbilities.LightSpirit.Sanctity.ChargeTime", 2500);
		
		config.addDefault("ExtraAbilities.LightSpirit.Shelter.Enable", true);
		config.addDefault("ExtraAbilities.LightSpirit.Shelter.Cooldown", 10000);
		config.addDefault("ExtraAbilities.LightSpirit.Shelter.Duration", 7000);
		config.addDefault("ExtraAbilities.LightSpirit.Shelter.Range", 20);
		config.addDefault("ExtraAbilities.LightSpirit.Shelter.ShieldSize", 5);
		config.addDefault("ExtraAbilities.LightSpirit.Shelter.KnockbackPower", 1);
		
		config.addDefault("ExtraAbilities.DarkSpirit.Intoxicate.Enable", true);
		config.addDefault("ExtraAbilities.DarkSpirit.Intoxicate.Cooldown", 5000);
		config.addDefault("ExtraAbilities.DarkSpirit.Intoxicate.Radius", 5);
		config.addDefault("ExtraAbilities.DarkSpirit.Intoxicate.PotionInterval", 2000);
		config.addDefault("ExtraAbilities.DarkSpirit.Intoxicate.HarmInterval", 5000);
		config.addDefault("ExtraAbilities.DarkSpirit.Intoxicate.ParticleColor (Has to be 6 characters)", "BD0000");
		
		config.addDefault("ExtraAbilities.DarkSpirit.Shackle.Enable", true);
		config.addDefault("ExtraAbilities.DarkSpirit.Shackle.Cooldown", 5000);
		config.addDefault("ExtraAbilities.DarkSpirit.Shackle.Duration", 2500);
		config.addDefault("ExtraAbilities.DarkSpirit.Shackle.Range", 20);
		config.addDefault("ExtraAbilities.DarkSpirit.Shackle.Radius", 2);
		
		ConfigManager.defaultConfig.save();
	}

	@Override
	public void stop() {
		
		super.remove();
		
	}
	
	@Override
	public boolean isHiddenAbility() {
		return true;
	}

	@Override
	public long getCooldown() {
		return 0;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "Handler";
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void progress() {

	}

	@Override
	public String getAuthor() {
		return null;
	}

	@Override
	public String getVersion() {
		return null;
	}

}
