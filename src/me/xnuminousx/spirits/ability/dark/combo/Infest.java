package me.xnuminousx.spirits.ability.dark.combo;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.DarkAbility;

public class Infest extends DarkAbility implements ComboAbility, AddonAbility {

	private boolean progress;
	private long cooldown;
	private Location location;
	private Location origin;
	private Vector direction;
	private int currPoint;
	private int range;
	private double radius;
	private long time;
	private long duration;
	private boolean firstEff;
	private boolean canUseOnSpirits;

	public Infest(Player player) {
		super(player);
		
		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}
		
		setFields();
		start();
		time = System.currentTimeMillis();
		bPlayer.addCooldown(this);
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.DarkSpirit.Combo.Infest.Cooldown");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.DarkSpirit.Combo.Infest.Duration");
		this.range = ConfigManager.getConfig().getInt("Abilities.Spirits.DarkSpirit.Combo.Infest.Range");
		this.radius = ConfigManager.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Combo.Infest.Radius");
		this.canUseOnSpirits = ConfigManager.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Combo.Infest.CanUseOtherSpirits");
		this.origin = player.getLocation().clone().add(0, 1, 0);
		this.location = origin.clone();
		this.direction = player.getLocation().getDirection();
		this.progress = true;
		this.firstEff = true;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location) || !bPlayer.canBendIgnoreBindsCooldowns(this)) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		if (origin.distanceSquared(location) > range * range) {
			bPlayer.addCooldown(this);
			remove();
			return;
			
		}
		swarm(20, 0.02F);
	}
	
	public void swarm(int points, float size) {
		if (progress) {
			location.add(direction.multiply(1));
			for (int i = 0; i < 6; i++) {
				currPoint += 360 / points;
				if (currPoint > 360) {
					currPoint = 0;
				}
				double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
				double x = size * (Math.PI * 4 - angle) * Math.cos(angle + i);
				double y = 0.6 * Math.cos(angle);
	            double z = size * (Math.PI * 4 - angle) * Math.sin(angle + i);
				location.add(x, y, z);
				ParticleEffect.WITCH_MAGIC.display(location, 0.6F, 0.6F, 0.6F, 0, 1);
				ParticleEffect.SMOKE.display(location, 0, 0, 0, 0, 1);
				location.subtract(x, y, z);
			}
		}
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(location, radius)) {
			if ((target instanceof LivingEntity || target instanceof Player) && !target.getUniqueId().equals(player.getUniqueId())) {
				this.progress = false;
				location = target.getLocation();
				LivingEntity le = (LivingEntity)target;
				
				if (!canUseOnSpirits) {
					if (target instanceof Player) {
						Player player = (Player)target;
						BendingPlayer bTarget = BendingPlayer.getBendingPlayer(player);
						if (bTarget.hasElement(Element.getElement("Spirit")) || bTarget.hasElement(Element.getElement("DarkSpirit")) || bTarget.hasElement(Element.getElement("LightSpirit"))) {
							bPlayer.addCooldown(this);
							remove();
							return;
						}
					}
				}
				
				if (System.currentTimeMillis() > time + duration) {
					bPlayer.addCooldown(this);
					remove();
					return;
				} else {
					ParticleEffect.SMOKE.display(location, 1, 1, 1, 0, 2);
					if (System.currentTimeMillis() > time + (duration / 4) && (firstEff)) {
						ParticleEffect.DRAGON_BREATH.display(location, 0.5F, 1F, 0.5F, 0.05F, 2);
						le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1), true);
					}
					
					if (System.currentTimeMillis() > time + (duration / 2)) {
						firstEff = false;
						le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 2), true);
					}
				}
			}
		}
	}

	@Override
	public Object createNewComboInstance(Player player) {
		return new Infest(player);
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		ArrayList<AbilityInformation> combo = new ArrayList<>();
		combo.add(new AbilityInformation("Intoxicate", ClickType.SHIFT_DOWN));
		combo.add(new AbilityInformation("Strike", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Strike", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Strike", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Strike", ClickType.SHIFT_UP));
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
		return "Infest";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("dark", "Combo") + 
				ConfigManager.languageConfig.get().getString("Abilities.DarkSpirit.Infest.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.spiritChatColor("dark") + ConfigManager.languageConfig.get().getString("Abilities.DarkSpirit.Infest.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.spiritChatColor("dark") + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.spiritChatColor("dark") + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Combo.Infest.Enabled");
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
