package me.xnuminousx.spirits.ability.light;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.SphereEffect;
import me.xnuminousx.spirits.ability.api.LightAbility;

public class Shield extends LightAbility implements AddonAbility {

	private long cooldown;
	private long duration;
	private int range;
	private Location loc;
	private Location origin;
	private Vector dir;

	public Shield(Player player) {
		super(player);

		start();
		setFields();
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.LightSpirit.Shelter.Cooldown");
		this.range = ConfigManager.getConfig().getInt("Abilities.LightSpirit.Shelter.Range");
		this.duration = ConfigManager.getConfig().getLong("Abilities.LightSpirit.Shelter.Duration");
		this.loc = player.getLocation();
		this.origin = player.getLocation();
		this.dir = player.getLocation().getDirection();
	}

	@Override
	public long getCooldown() {
		return this.cooldown;
	}

	@Override
	public Location getLocation() {
		return this.loc;
	}

	@Override
	public String getName() {
		return "Shield";
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
	public void progress() {
		if (!bPlayer.canBend(this)) {
			remove();
			return;
		}

		EffectManager em = new EffectManager(ProjectKorra.plugin);
		if (player.isSneaking()) {
			Effect effect = new SphereEffect(em);
			effect.setEntity(player);
			effect.color.setRed(255).setGreen(255).setBlue(0);
			effect.start();
			bPlayer.addCooldown(this);
		}

		if (System.currentTimeMillis() - getStartTime() > this.duration) {
			em.dispose();
			remove();
			bPlayer.addCooldown(this);
			return;
		}

		if (origin.distanceSquared(loc) > range * range) {
			remove();
			bPlayer.addCooldown(this);
			return;

		}
		
		loc.add(dir).multiply(1);
		GeneralMethods.displayColoredParticle(loc, "#ffff00");

		for (Entity target : GeneralMethods.getEntitiesAroundPoint(loc, 1)) {
			if (((target instanceof LivingEntity)) && (target.getEntityId() != player.getEntityId())) {
				Effect eff = new SphereEffect(em);
				eff.setEntity(target);
				eff.color.setRed(255).setGreen(255).setBlue(0);
				eff.start();
				bPlayer.addCooldown(this);
			}
		}

	}

	@Override
	public String getAuthor() {
		return "EmeraldJelly";
	}

	@Override
	public String getVersion() {
		return "v1.0.0";
	}

	@Override
	public void load() {

	}

	@Override
	public void stop() {

	}

}
