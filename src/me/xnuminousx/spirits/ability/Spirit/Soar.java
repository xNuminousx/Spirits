package me.xnuminousx.spirits.ability.Spirit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.ability.API.SpiritAbility;
import net.md_5.bungee.api.ChatColor;

public class Soar extends SpiritAbility implements AddonAbility {

	private boolean enable;
	private boolean isHidden;
	private Location location;
	private long cooldown;
	private Material blockType;
	private byte blockByte;
	private long time;
	private long duration;
	private double speed;

	public Soar(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		
		start();
	}

	private void setFields() {
		this.enable = Main.plugin.getConfig().getBoolean("Abilities.Spirits.Soar.Enable");
		this.cooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.Soar.Cooldown");
		this.duration = Main.plugin.getConfig().getLong("Abilities.Spirits.Soar.Duration");
		this.speed = Main.plugin.getConfig().getDouble("Abilities.Spirits.Soar.Speed");
		isHidden = false;
		enable = true;
		this.blockType = Material.SNOW;
		this.location = player.getLocation();
	}

	@Override
	public void progress() {
		if (!enable) {
			isHidden = true;
			remove()	;
			return;
		}
		
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
			remove();
			return;
		}
		if (player.isSneaking()) {
			fly();
		} else {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
	}
	
	public void fly() {
		if (System.currentTimeMillis() > time + duration) {
			bPlayer.addCooldown(this);
			remove();
			return;
		} else {
			Location location = player.getLocation();
			Vector vec = player.getLocation().getDirection().normalize().multiply(speed);
			player.setVelocity(vec);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.3F, 5F);
			ParticleEffect.BLOCK_CRACK.display((ParticleEffect.ParticleData) new ParticleEffect.BlockData(blockType, blockByte), 0.2F, 0.2F, 0.2F, 1, 5, location, 500);
		}
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
		return "Soar";
	}
	
	@Override
	public String getDescription() {
		return ChatColor.BLUE + "" + ChatColor.BOLD + "Mobility: " + ChatColor.DARK_AQUA + "A key aspect of all Spirits is their weightlessness which allows them to soar through the skies as if gravity is non existant, which is exactly what this ability allows you to do!";
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.BLUE + "Hold shift";
	}

	@Override
	public String getAuthor() {
		return ChatColor.BLUE + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return ChatColor.BLUE + "1.0";
	}
	
	public boolean isHiddenAbility() {
		return isHidden;
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
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void load() {

	}

	@Override
	public void stop() {

	}

}
