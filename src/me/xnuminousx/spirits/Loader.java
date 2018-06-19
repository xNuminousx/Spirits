package me.xnuminousx.spirits;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AvatarAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.listeners.AbilityListener;
import me.xnuminousx.spirits.listeners.PassiveListener;

public class Loader extends AvatarAbility implements AddonAbility {

	public Loader(Player player) {
		super(player);
	}

	@Override
	public void load() {
		FileConfiguration langConfig = ConfigManager.languageConfig.get();
		FileConfiguration config = ConfigManager.getConfig();
		
		//Register Listeners
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new AbilityListener(), ProjectKorra.plugin);
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new PassiveListener(), ProjectKorra.plugin);
	
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
		
		//Descriptions & Instructions
		langConfig.addDefault("Abilities.Spirit.Agility.Description", "This ability offers you 2 modes of mobility. The first being the ability to dash forward very quickly. The second being the ability to soar through the skies as if gravity is non-existant.");
		langConfig.addDefault("Abilities.Spirit.Agility.Instructions", "Left-Click: Dash ⏐ Hold shift: Soar");
		/*
		 * 
		langConfig.addDefault("Abilities.Spirit.Fuse.Description", "A rarely seen ability of Spirits is the ability to Fuse with a being, combine your energies, and empower your host. If the host is a human, they will enter the Avatar State only while being fused. This is very dangerous for the human, however, because if a spirit fuses with a human for too long, the human will die immediately. A spirit must wait 1 second after fusing to be able to exit the even. To exit, tap shift.");
		langConfig.addDefault("Abilities.Spirit.Fuse.Instructions", "Possess (Left-click) > Agility (Left-click) > Agility (Hold shift)");
		*/
		
		langConfig.addDefault("Abilities.Spirit.Possess.Description", "A very ancient ability of Spitits is the ability to jump inside the body of a human. As a Spirit, you'll be able to possess a human for a short amount of time and do harm. While this ability is active, both the Spirit and the target will be motionless.");
		langConfig.addDefault("Abilities.Spirit.Possess.Instructions", "Hold shift and look at a human.");
		
		langConfig.addDefault("Abilities.Spirit.Phase.Description", "This advanced combo allows a Spirit to dematerialize into a state where they can walk through walls and fly around a certain radius. They are able to do this because of their unqiue molecular makeup not seen in any other being!");
		langConfig.addDefault("Abilities.Spirit.Phase.Instructions", "Vanish (Left-click 2x) > Possess (Tap-shift) > Vanish (Left-click) ⏐ To Exit: Vanish (Tap-shift)");
		
		langConfig.addDefault("Abilities.Spirit.Vanish.Description", "Spirits are often seen disappearing into thin air and then reappearing somewhere different. With this ability, you can harness that power as well! However, there is a certain duration you are able to vanish for an a radius of how far away from your original location you're allowed to get!");
		langConfig.addDefault("Abilities.Spirit.Vanish.Instructions", "Hold shift: Disappear ⏐ Release shift: Reappear");
		
		langConfig.addDefault("Abilities.LightSpirit.Alleviate.Description", "The healing ability for LightSpirits, this allows you to heal yourself and others! When healing, whoever is being healed will be removed of ANY negative potion effects aswell as recieve regeneration for a period of time.");
		langConfig.addDefault("Abilities.LightSpirit.Alleviate.Instructions", "Hold Shift while lookat at a target: Heal them ⏐ Hold Shift while looking away: Heal yourself.");
		
		langConfig.addDefault("Abilities.LightSpirit.Rejuvenate.Description", "After executing the combo sequence, you will mark the ground with positively charged spiritual energy for a duration of time. Entities can come to this location to heal themselves, but dark creatures must beware!");
		langConfig.addDefault("Abilities.LightSpirit.Rejuvenate.Instructions", "Alleviate (Shift down) > Alleviate (Right-Click block) > Alleviate (Release shift)");
		
		langConfig.addDefault("Abilities.LightSpirit.Orb.Description", "Plant an orb of positive energy on the ground which awaits for oncoming entities. If the orb detects something moving past it, it'll expand and harm anyone in its wake! More features to come...");
		langConfig.addDefault("Abilities.LightSpirit.Orb.Instructions", "Hold shift until you see particles. Release shift while looking at an area nearby on the ground to plant it there.");
		
		langConfig.addDefault("Abilities.DarkSpirit.Intoxicate.Description", "Sacrifice some of your energy to pour a bit of chaos into the souls of your nearby enemies by taking away their positive potion effects and adding negative ones. Then watch as it destroys them from the inside out! The great spirit Vaatu was known to have this influence over other unbalanced Spirits.");
		langConfig.addDefault("Abilities.DarkSpirit.Intoxicate.Instructions", "Hold shift");
		
		langConfig.addDefault("Abilities.LightSpirit.Shelter.Description", "A very useful tactic when group battling, a light spirit can temporarily shield a friend or even a foe from incoming enemies. Additionally, they have the options to shield themselves!");
		langConfig.addDefault("Abilities.LightSpirit.Shelter.Instructions", "Left click: Shield others ⏐ Hold shift: Shield yourself.");
		
		langConfig.addDefault("Abilities.DarkSpirit.Infest.Description", "After executing the combo sequence, you will mark the ground with negatively charged spiritual energy for a duration of time. Monsters can come to this location for strength, but any other entities must beware!");
		langConfig.addDefault("Abilities.DarkSpirit.Infest.Instructions", "Intoxicate (Shift down) > Intoxicate (Right-Click block) > Intoxicate (Release shift)");
		
		langConfig.addDefault("Abilities.DarkSpirit.Shackle.Description", "With this technique a DarkSpirit is able to temporarily trap an anyone dead in their tracks, even if you can't see them! Useful for a quick get away...");
		langConfig.addDefault("Abilities.DarkSpirit.Shackle.Instructions", "Left click");
		
		langConfig.addDefault("Abilities.DarkSpirit.Strike.Description", "The most basic ability of an aggressive, unbalanced Spirit is to rush towards their enemy and try to bite them in 1 swift motion. When you activate this ability, you'll see your target zone. If your target zone gets in range of another entity, you'll be rushed over to them an deal damage.");
		langConfig.addDefault("Abilities.DarkSpirit.Strike.Instructions", "Left-Click to trigger target spectacle");
		
		langConfig.addDefault("Abilities.Water.Purify.Description", "A rare technique practice amongst waterbenders is the ability to influence a Spirits nature towards either the darkness or the light. Using this ability, you can influence a DarkSpirit to be peaceful and guide them towards the light.");
		langConfig.addDefault("Abilities.Water.Purify.Instructions", "Hold shift while looking at an entity");
		
		langConfig.addDefault("Abilities.Water.Corrupt.Description", "A rare technique practice amongst waterbenders is the ability to influence a Spirits nature towards either the darkness or the light. Using this ability, you can influence a LightSpirit to be chaotic and follow the will of the great DarkSpirit, Vatuu.");
		langConfig.addDefault("Abilities.Water.Corrupt.Instructions", "Hold shift while looking at an entity");
		
		//Ability configuration
		config.addDefault("Abilities.Spirits.Neutral.Agility.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Agility.Dash.Cooldown", 2000);
		config.addDefault("Abilities.Spirits.Neutral.Agility.Dash.Distance", 3);
		config.addDefault("Abilities.Spirits.Neutral.Agility.Soar.Cooldown", 4500);
		config.addDefault("Abilities.Spirits.Neutral.Agility.Soar.Duration", 1000);
		config.addDefault("Abilities.Spirits.Neutral.Agility.Soar.Speed", 0.8);
		
		config.addDefault("Abilities.Spirits.Neutral.Possess.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Possess.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.Neutral.Possess.Radius", 5);
		config.addDefault("Abilities.Spirits.Neutral.Possess.Damage", 5);
		config.addDefault("Abilities.Spirits.Neutral.Possess.Duration", 2000);
		
		config.addDefault("Abilities.Spirits.Neutral.Vanish.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Vanish.Cooldown", 7000);
		config.addDefault("Abilities.Spirits.Neutral.Vanish.Duration", 4000);
		config.addDefault("Abilities.Spirits.Neutral.Vanish.Range", 20);
		config.addDefault("Abilities.Spirits.Neutral.Vanish.Radius", 10);
		config.addDefault("Abilities.Spirits.Neutral.Vanish.RemoveFire", true);
		
		/*
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.Cooldown", 20000);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.Duration", 10000);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.Players.KillAfterDuration", true);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.NonPlayers.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.NonPlayers.BuffDuration", 10);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Fuse.NonPlayers.HarmfulFuseDelay", 5000);
		*/
		
		config.addDefault("Abilities.Spirits.Neutral.Combo.Phase.Enabled", true);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Phase.Cooldown", 20000);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Phase.Duration", 10000);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Phase.Range", 10);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Phase.MinHealth", 6);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Phase.Vanish.ApplyCooldown", true);
		config.addDefault("Abilities.Spirits.Neutral.Combo.Phase.Vanish.Cooldown", 20000);
		
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Enabled", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Others.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Others.Range", 5);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Others.PotionInterval", 2000);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Others.HealInterval", 5000);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Others.ParticleColor (Has to be 6 characters)", "FFFFFF");
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Self.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Self.ChargeTime", 2000);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Self.HealDuration", 1.5);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Self.NightVisionDuration", 1.5);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Self.RemoveNegativePotionEffects", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Alleviate.Self.ParticleColor (Has to be 6 characters)", "FFFFFF");
		
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.Enabled", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.Cooldown", 10000);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.ChargeTime", 2000);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.Duration", 30000);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.Damage", 3);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.PlaceRange", 20);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.DetonateRange", 3);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.EffectRange", 5);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.BlindnessDuration", 120);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.NauseaDuration", 300);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.PotionPower", 2);
		config.addDefault("Abilities.Spirits.LightSpirit.Orb.RequireGround", true);
		
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Enabled", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.RemoveOnDamage", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Duration", 7000);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Range", 20);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Others.ClickDelay", 2000);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Others.Cooldown", 10000);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Self.Cooldown", 10000);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Others.ShieldSize", 5);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Self.ShieldSize", 4);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Others.KnockbackPower", 1);
		config.addDefault("Abilities.Spirits.LightSpirit.Shelter.Self.KnockbackPower", 1);
		
		config.addDefault("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Enabled", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Cooldown", 15000);
		config.addDefault("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Duration", 10000);
		config.addDefault("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Radius", 5);
		config.addDefault("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Damage", 1);
		config.addDefault("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.EffectInterval", 10);
		config.addDefault("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.HurtDarkSpirits", true);
		config.addDefault("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.HurtMonsters", true);
		
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
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Cooldown", 15000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Duration", 10000);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Radius", 8);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.EffectInterval", 10);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.Damage", 1);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.DamageEntities", true);
		config.addDefault("Abilities.Spirits.DarkSpirit.Combo.Infest.HealDarkSpirits", true);
		
		config.addDefault("Abilities.Spirits.Water.Purify.Enabled", true);
		config.addDefault("Abilities.Spirits.Water.Purify.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.Water.Purify.Duration", 20000);
		config.addDefault("Abilities.Spirits.Water.Purify.Range", 10);
		config.addDefault("Abilities.Spirits.Water.Purify.SetElement", true);
		
		config.addDefault("Abilities.Spirits.Water.Corrupt.Enabled", true);
		config.addDefault("Abilities.Spirits.Water.Corrupt.Cooldown", 5000);
		config.addDefault("Abilities.Spirits.Water.Corrupt.Duration", 20000);
		config.addDefault("Abilities.Spirits.Water.Corrupt.Range", 10);
		config.addDefault("Abilities.Spirits.Water.Corrupt.SetElement", true);
		
		//Save default configurations
		ConfigManager.defaultConfig.save();
		ConfigManager.languageConfig.save();
		
		//Project console load message
		ProjectKorra.log.info("Successfully enabled Spirits " + Methods.getVersion());
	}

	@Override
	public void stop() {
		//Remove all project actions
		super.remove();
		
		//Project console disable message
		ProjectKorra.log.info("Successfully disabled Spirits " + Methods.getVersion());
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
