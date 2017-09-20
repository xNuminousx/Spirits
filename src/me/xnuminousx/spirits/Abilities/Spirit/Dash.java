package me.xnuminousx.spirits.Abilities.Spirit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.Abilities.API.SpiritAbility;
import me.xnuminousx.spirits.Listeners.AbilityListener;
import net.md_5.bungee.api.ChatColor;

public class Dash extends SpiritAbility implements AddonAbility {
	
	private Location location;
	private long distance;
	private Material blockType;
	private byte blockByte;
	private long cooldown;
	private boolean enable;
	private boolean isHidden;

	public Dash(Player player) {
		super(player);
		
		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		start();
	}

	private void setFields() {
		this.enable = Main.plugin.getConfig().getBoolean("Abilities.Spirits.Dash.Enable");
		this.cooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.Dash.Cooldown");
		this.distance = Main.plugin.getConfig().getLong("Abilities.Spirits.Dash.Distance");
		this.location = player.getLocation();
		this.isHidden = false;
		this.blockType = Material.LAPIS_BLOCK;
	}

	@Override
	public void progress() {
		if (!enable) {
			isHidden = true;
			remove()	;
			return;
		}
		
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location) || !com.projectkorra.projectkorra.GeneralMethods.isSolid(player.getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN))) {
			remove();
			return;
		}
		dash();
		bPlayer.addCooldown(this);
		remove();
		return;
	}
	
	public void dash() {
		Location loc = player.getLocation();
		Vector vec = loc.getDirection().normalize().multiply(distance);
		vec.setY(0.2);
		player.setVelocity(vec);
		loc.getWorld().playSound(location, Sound.ENTITY_ELDER_GUARDIAN_HURT, 1.5F, 0.5F);
		loc.getWorld().playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.3F, 0.5F);
		ParticleEffect.BLOCK_CRACK.display((ParticleEffect.ParticleData) new ParticleEffect.BlockData(blockType, blockByte), 0.5F, 0.5F, 0.5F, 1, 30, loc, 500);
		return;
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "Dash";
	}
	
	@Override
	public String getDescription() {
		return ChatColor.BLUE + "" + ChatColor.BOLD + "Mobility: " + ChatColor.DARK_AQUA + "Sometimes, in intense battles, a spirit may dart from one location to the next! Useful to escape, evade or just plain exploring.";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.BLUE + "Left-Click";
	}

	@Override
	public String getAuthor() {
		return ChatColor.BLUE + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return ChatColor.BLUE + "1.0";
	}

	@Override
	public boolean isExplosiveAbility() {
		return false;
	}
	
	@Override
	public boolean isHiddenAbility() {
		return isHidden;
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
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new AbilityListener(), ProjectKorra.plugin);

	}

	@Override
	public void stop() {
		super.remove();

	}

}
