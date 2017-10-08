package me.xnuminousx.spirits.ability.spirit.combo;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class Fuse extends SpiritAbility implements AddonAbility, ComboAbility {

	private long cooldown;
	private Location origin;
	private int distance;
	private int strengthDuration;
	private int weaknessDuration;

	public Fuse(Player player) {
		super(player);

		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}
		
		setFields();
		
		start();
		bPlayer.addCooldown(this);
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Fuse.Cooldown");
		this.distance = ConfigManager.getConfig().getInt("Abilities.Spirits.Neutral.Combo.Fuse.Distance");
		this.strengthDuration = ConfigManager.getConfig().getInt("Abilities.Spirits.Neutral.Combo.Fuse.StrengthDuration");
		this.weaknessDuration = ConfigManager.getConfig().getInt("Abilities.Spirits.Neutral.Combo.Fuse.WeaknessDuration");
		this.origin = player.getLocation();
		this.distance = 10;
		this.strengthDuration = 2;
		this.weaknessDuration = 2;
		
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, origin) || !bPlayer.canBendIgnoreBindsCooldowns(this)) {
			remove();
			return;
		}
		
		if (!player.isSneaking() || origin.distanceSquared(player.getLocation()) > distance * distance) {
			remove();
			return;
		} else {
			dash();
		}
		
	}
	
	public void dash() {
		Vector direction = player.getLocation().getDirection().normalize().multiply(2);
		player.setVelocity(direction);
		Methods.spiritParticles(bPlayer, player.getLocation(), 0.5F, 0.5F, 0.5F, 0, 10);
		
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(player.getLocation(), 1)) {
			if (((target instanceof LivingEntity)) && (target.getEntityId() != player.getEntityId())) {
				Vector newDir = player.getLocation().getDirection().normalize().multiply(0);
				player.setVelocity(newDir);
				
				LivingEntity le = (LivingEntity)target;
				ParticleEffect.DRAGON_BREATH.display(target.getLocation(), 0.5F, 1, 0.5F, 0.08F, 10);
				le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, strengthDuration * 100, 0), true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, weaknessDuration * 100, 0), true);
				
				remove();
				return;
			}
		}
	}

	@Override
	public Object createNewComboInstance(Player player) {
		return new Fuse(player);
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		ArrayList<AbilityInformation> combo = new ArrayList<>();
		combo.add(new AbilityInformation("Soar", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Soar", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Possess", ClickType.SHIFT_DOWN));
		return combo;
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
		return "Fuse";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("spirit", "Combo", "Rush towards a human to combine your energies and temporarily empower them with strength! This will come at a cost of your own power and strength, however. An alternative usage is a powerful boost.");
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.BLUE + "Soar (Left-click 2x) > Possess (Hold shift)";
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
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Fuse.Enable");
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
		return true;
	}

	@Override
	public void load() {
		
	}

	@Override
	public void stop() {
		
	}

}
