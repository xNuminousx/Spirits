package me.xnuminousx.spirits.Abilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

public class Basic extends SpiritAbility implements AddonAbility {

	private Permission perm;

	public Basic(Player player) {
		super(player);
	}

	@Override
	public void load() {
		
		
		ProjectKorra.log.info("Successfully loaded Spirits");
		
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.Avatar", "WHITE");
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.AvatarSub", "AQUA");
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.DarkAvatar", "DARK_RED");
		
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.Spirit", "DARK_AQUA");
		ConfigManager.languageConfig.get().addDefault("Chat.Colors.SpiritSub", "DARK_PURPLE");
		ConfigManager.languageConfig.get().addDefault("Chat.Prefixes.Spirit", "[Spirit]");
		ConfigManager.languageConfig.get().addDefault("Chat.Prefixes.SpiritSub", "[Spirit]");
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Dash.Enable", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Dash.Cooldown", 2000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Dash.Distance", 3);
		perm = new Permission("bending.ability.dash");
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
		perm.setDefault(PermissionDefault.TRUE);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Possess.Enable", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Possess.Cooldown", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Possess.Radius", 5);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Possess.Damage", 5);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Possess.Duration", 2000);
		perm = new Permission("bending.ability.possess");
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
		perm.setDefault(PermissionDefault.TRUE);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Alleviate.Enable", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Alleviate.Cooldown", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Alleviate.Radius", 5);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Alleviate.PotionInterval", 2000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Alleviate.HealInterval", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Alleviate.ParticleColor (Has to be 6 characters)", "FFFFFF");
		perm = new Permission("bending.ability.alleviate");
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
		perm.setDefault(PermissionDefault.TRUE);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Intoxicate.Enable", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Intoxicate.Cooldown", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Intoxicate.Radius", 5);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Intoxicate.PotionInterval", 2000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Intoxicate.HarmInterval", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Intoxicate.ParticleColor (Has to be 6 characters)", "BD0000");
		perm = new Permission("bending.ability.intoxicate");
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
		perm.setDefault(PermissionDefault.TRUE);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Shackle.Enable", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Shackle.Cooldown", 5000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Shackle.Duration", 2500);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Shackle.Range", 20);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Shackle.Radius", 2);
		perm = new Permission("bending.ability.shackle");
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
		perm.setDefault(PermissionDefault.TRUE);
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Soar.Enable", true);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Soar.Cooldown", 4500);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Soar.Duration", 1000);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Spirits.Soar.Speed", 0.8);
		perm = new Permission("bending.ability.soar");
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
		perm.setDefault(PermissionDefault.TRUE);
		
		ConfigManager.defaultConfig.save();
	}

	@Override
	public void stop() {
		super.remove();
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
		return "";
	}
	
	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getAuthor() {
		return "";
	}

	@Override
	public String getVersion() {
		return "";
	}

	@Override
	public boolean isExplosiveAbility() {
		return false;
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public boolean isIgniteAbility() {
		return false;
	}
	
	@Override
	public boolean isHiddenAbility() {
		return true;
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void progress() {
		
	}

}
