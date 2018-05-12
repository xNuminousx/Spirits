package me.xnuminousx.spirits.ability.light;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.LightAbility;
import net.md_5.bungee.api.ChatColor;

public class Orb extends LightAbility implements AddonAbility {

	private long time;
	private long cooldown;
	private Location location;
	private Location targetLoc;
	private long chargeTime;
	private boolean isCharged;
	private long duration;

	public Orb(Player player) {
		super(player);

		if (!bPlayer.canBend(this)) {
			return;
		}
		
		setFields();
		time = System.currentTimeMillis();
		start();
	}

	private void setFields() {
		this.location = player.getLocation();
		this.chargeTime = 2000;
		this.isCharged = false;
		this.duration = 10000;
		this.targetLoc = GeneralMethods.getTargetedLocation(player, 10);
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
			remove();
			return;
		}
		if (player.isSneaking()) {
			if (System.currentTimeMillis() > time + chargeTime) {
				isCharged = true;
				ParticleEffect.FLAME.display(player.getLocation(), 0, 0, 0, 0.07F, 2);
			}
		} else if (isCharged) {
			ParticleEffect.FLAME.display(targetLoc, 0, 0, 0, 0.07F, 2);
			if (System.currentTimeMillis() > time + duration) {
				remove();
				return;
			}
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

		return "Orb";
	}
	
	@Override
	public String getDescription() {
		return ChatColor.BOLD + "COMING SOON: " + "With this upcoming ability, LightSpirits will be able to summon an Orb of light which you can plant at a location. If someone gets within radius of this orb, it will begin to expand and the light energy will harm them.";
	}
	
	@Override
	public String getInstructions() {
		return "Coming soon!";
	}

	@Override
	public String getAuthor() {

		return Methods.getAuthor();
	}

	@Override
	public String getVersion() {

		return Methods.getVersion();
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
