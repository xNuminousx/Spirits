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
	
		//Language config
		langConfig.addDefault("Chat.Colors.Spirit", "DARK_AQUA");
		langConfig.addDefault("Chat.Colors.SpiritSub", "DARK_PURPLE");
		langConfig.addDefault("Chat.Colors.LightSpirit", "AQUA");
		langConfig.addDefault("Chat.Colors.LightSpiritSub", "WHITE");
		langConfig.addDefault("Chat.Colors.DarkSpirit", "BLUE");
		langConfig.addDefault("Chat.Colors.DarkSpiritSub", "DARK_GRAY");
		langConfig.addDefault("Chat.Prefixes.Spirit", "[Spirit]");
		langConfig.addDefault("Chat.Prefixes.LightSpirit", "[LightSpirit]");
		langConfig.addDefault("Chat.Prefixes.DarkSpirit", "[DarkSpirit]");
		
		langConfig.addDefault("Abilities.Spirit.Dash.Description", "Sometimes, in intense battles, a Spirit may dart from one location to the next! Useful to escape, evade or just plain exploring.");
		langConfig.addDefault("Abilities.Spirit.Dash.Instructions", "Left-Click");
		
		langConfig.addDefault("Abilities.Spirit.Soar.Description", "A key aspect of all Spirits is their weightlessness which allows them to soar through the skies as if gravity is non-existant, which is exactly what this ability allows you to do!");
		langConfig.addDefault("Abilities.Spirit.Soar.Instructions", "Hold shift");
		
		langConfig.addDefault("Abilities.Spirit.Possess.Description", "A very ancient ability of Spitits is the ability to jump inside the body of a human. As a Spirit, you'll be able to possess a human for a short amount of time and do harm. While this ability is active, both the Spirit and the target will be motionless.");
		langConfig.addDefault("Abilities.Spirit.Possess.Instructions", "Hold shift and look at a human.");
		
		langConfig.addDefault("Abilities.Spirit.Fuse.Description", "Rush towards a human to combine your energies and temporarily empower them with strength! This will come at a cost of your own power and strength, however. You must collide with your target for this to be used successfully. An alternative usage is a powerful boost.");
		langConfig.addDefault("Abilities.Spirit.Fuse.Instructions", "Soar (Left-click 2x) > Possess (Hold shift)");
		
		langConfig.addDefault("Abilities.LightSpirit.Alleviate.Description", "Use this ability to relieve your friends and allies of their negative potion effects, keep using it and you'll give them a small boost of your own health. If your target moves, the ability will cancel.");
		langConfig.addDefault("Abilities.LightSpirit.Alleviate.Instructions", "Hold shift while looking at your target");
		
		langConfig.addDefault("Abilities.LightSpirit.Shelter.Description", "A very useful tactic when group battling, a light spirit can temporarily shield a friend or even a foe from incoming enemies.");
		langConfig.addDefault("Abilities.LightSpirit.Shelter.Instructions", "Left click");
		
		langConfig.addDefault("Abilities.LightSpirit.Sanctity.Description", "Use this to empower yourself with more health, better resistance and the ability to find the light in the dark for a limited period of time.");
		langConfig.addDefault("Abilities.LightSpirit.Sanctity.Instructions", "Hold shift until you see the trigger");
		
		langConfig.addDefault("Abilities.DarkSpirit.Intoxicate.Description", "Sacrifice some of your energy to pour a bit of chaos into the souls of your nearby enemies by taking away their positive potion effects and adding negative ones. Then watch as it destroys them from the inside out! The great spirit Vaatu was known to have this influence over other unbalanced Spirits.");
		langConfig.addDefault("Abilities.DarkSpirit.Intoxicate.Instructions", "Hold shift");
		
		langConfig.addDefault("Abilities.DarkSpirit.Shackle.Description", "With this technique a DarkSpirit is able to temporarily trap an anyone dead in their tracks, even if you can't see them! Useful for a quick get away...");
		langConfig.addDefault("Abilities.DarkSpirit.Shackle.Instructions", "Left click");
		
		langConfig.addDefault("Abilities.DarkSpirit.Strike.Description", "The most basic ability of an aggressive, unbalanced Spirit is to rush towards their enemy and try to bite them in 1 swift motion. When you activate this ability, you'll see your target zone. If your target zone gets in range of another entity, you'll be rushed over to them an deal damage.");
		langConfig.addDefault("Abilities.DarkSpirit.Strike.Instructions", "Left-Click to trigger target spectacle");
		
		langConfig.addDefault("Abilities.DarkSpirit.Infest.Description", "A very dangerous combo; used in offense to attack players and infest them with the influence of darkness. When your swarm attaches to an entity, it will begin to damage them then leave them temporarily blind! This ability will not work against other Spirits.");
		langConfig.addDefault("Abilities.DarkSpirit.Infest.Instructions", "Intoxicate (Hold shift) > Strike (Left-click x3) > Strike (Release shift)");
		
		//Ability config
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
		ConfigManager.languageConfig.save();
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
