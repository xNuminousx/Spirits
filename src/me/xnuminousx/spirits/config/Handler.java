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
		FileConfiguration langConfig = ConfigManager.languageConfig.get();
		FileConfiguration config = ConfigManager.getConfig();
		
		//Register Listener
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new AbilityListener(), ProjectKorra.plugin);
		
		//Project console load message
		ProjectKorra.log.info("Successfully loaded Spirits");
	
		langConfig.addDefault("Chat.Colors.Spirit", "DARK_AQUA");
		langConfig.addDefault("Chat.Colors.SpiritSub", "DARK_PURPLE");
		langConfig.addDefault("Chat.Colors.LightSpirit", "AQUA");
		langConfig.addDefault("Chat.Colors.LightSpiritSub", "WHITE");
		langConfig.addDefault("Chat.Colors.DarkSpirit", "BLUE");
		langConfig.addDefault("Chat.Colors.DarkSpiritSub", "DARK_GRAY");
		langConfig.addDefault("Chat.Prefixes.Spirit", "[Spirit]");
		langConfig.addDefault("Chat.Prefixes.LightSpirit", "[LightSpirit]");
		langConfig.addDefault("Chat.Prefixes.DarkSpirit", "[DarkSpirit]");
		
		config.addDefault("Abilities.Spirits.Neutral.Dash.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Dash.Cooldown", 2000);
		config.addDefault("Abilities.Spirits.Neutral.Dash.Distance", 3);
		
		config.addDefault("Abilities.Spirits.Neutral.Possess.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Possess.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.Neutral.Possess.Radius", 5);
		config.addDefault("Abilities.Spirits.Neutral.Possess.Damage", 5);
		config.addDefault("Abilities.Spirits.Neutral.Possess.Duration", 2000);
		
		config.addDefault("Abilities.Spirits.Neutral.Soar.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Soar.Cooldown", 4500);
		config.addDefault("Abilities.Spirits.Neutral.Soar.Duration", 1000);
		config.addDefault("Abilities.Spirits.Neutral.Soar.Speed", 0.8);
		
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.Cooldown", 8000);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.Distance", 10);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.StrengthDuration", 2);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.WeaknessDuration", 2);
		
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Enabled", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Radius", 5);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.PotionInterval", 2000);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.HealInterval", 5000);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.ParticleColor (Has to be 6 characters)", "FFFFFF");
		
		config.addDefault("Abilities.Spirits.LightSpirit.Sanctity.Enabled", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Sanctity.Cooldown", 15000);
		config.addDefault("Abilities.Spirits.LightSpirit.Sanctity.Power", 2);
		config.addDefault("Abilities.Spirits.LightSpirit.Sanctity.PotionDuration", 1.5);
		config.addDefault("Abilities.Spirits.LightSpirit.Sanctity.ChargeTime", 2500);
		
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Enabled", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Cooldown", 10000);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Duration", 7000);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Range", 20);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.ShieldSize", 5);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.KnockbackPower", 1);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.RemoveOnDamage", true);
		
		config.addDefault("Abilities.Spirits.DarkSpirit.Intoxicate.Enabled", true);
		config.addDefault("Abilities.Spirits.DarkSpirit.Intoxicate.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Intoxicate.Radius", 5);
		config.addDefault("Abilities.Spirits.DarkSpirit.Intoxicate.PotionInterval", 2000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Intoxicate.HarmInterval", 5000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Intoxicate.ParticleColor (Has to be 6 characters)", "BD0000");
		
		config.addDefault("Abilities.Spirits.DarkSpirit.Shackle.Enabled", true);
		config.addDefault("Abilities.Spirits.DarkSpirit.Shackle.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Shackle.Duration", 2500);
		config.addDefault("Abilities.Spirits.DarkSpirit.Shackle.Range", 20);
		config.addDefault("Abilities.Spirits.DarkSpirit.Shackle.Radius", 2);
		
		config.addDefault("Abilities.Spirits.DarkSpirit.Strike.Enabled", true);
		config.addDefault("Abilities.Spirits.DarkSpirit.Strike.Cooldown", 4000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Strike.Range", 5);
		config.addDefault("Abilities.Spirits.DarkSpirit.Strike.Damage", 3);
		
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Enabled", true);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Cooldown", 10000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Duration", 5000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Range", 20);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Radius", 2);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.CanUseOnOtherSpirits", false);
		
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
