package me.xnuminousx.spirits.ability.dark.combo;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.Methods.SpiritType;
import me.xnuminousx.spirits.ability.api.DarkAbility;

public class Infest extends DarkAbility implements AddonAbility, ComboAbility {

	private Location location;
	private Location location2;
	private Location circleCenter;
	private long time;
	private long cooldown;
	private long duration;
	private int radius;
	private int effectInt;
	private boolean damageEntities;
	private boolean healDarkSpirits;
	private double damage;
	private int currPoint;
	private Location location3;

	public Infest(Player player) {
		super(player);
		
		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}
		setFields();
		time = System.currentTimeMillis();
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.3F, -1);
		start();
		bPlayer.addCooldown(this);
	}

	private void setFields() {
		this.cooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.Combo.Infest.Cooldown");
		this.duration = Main.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.Combo.Infest.Duration");
		this.radius = Main.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Combo.Infest.Radius");
		this.effectInt = Main.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Combo.Infest.EffectInterval");
		this.damage = Main.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Combo.Infest.Damage");
		this.damageEntities = Main.plugin.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Combo.Infest.DamageEntities");
		this.healDarkSpirits = Main.plugin.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Combo.Infest.HealDarkSpirits");
		location = player.getLocation();
		location2 = player.getLocation();
		location3 = player.getLocation();
		circleCenter = player.getLocation();
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location) || !bPlayer.canBendIgnoreBindsCooldowns(this)) {
			remove();
			return;
		}
		spawnCircle();
		grabEntities();
		if (System.currentTimeMillis() > time + duration) {
			remove();
			return;
		}
		
	}
	
	public void spawnCircle() {
		Methods.createPolygon(location, 8, radius, 0.2, ParticleEffect.SPELL_WITCH);
		for (int i = 0; i < 6; i++) {
			this.currPoint += 360 / 300;
			if (this.currPoint > 360) {
				this.currPoint = 0;
			}
			double angle = this.currPoint * Math.PI / 180.0D;
			double x = radius * Math.cos(angle);
			double x2 = radius * Math.sin(angle);
			double z = radius * Math.sin(angle);
			double z2 = radius * Math.cos(angle);
			location2.add(x, 0, z);
			ParticleEffect.SMOKE.display(location2, 0, 0, 0, 0, 1);
			location2.subtract(x, 0, z);
			
			location3.add(x2, 0, z2);
			ParticleEffect.SMOKE.display(location3, 0, 0, 0, 0, 1);
			location3.subtract(x2, 0, z2);
		}
		
		ParticleEffect.DRAGON_BREATH.display(location, radius / 2, 0.4F, radius / 2, 0.01F, 1);
	}
	
	public void grabEntities() {
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(circleCenter, radius)) {
			if (entity instanceof LivingEntity) {
				infestEntities(entity);
			}
		}
	}
	
	public void infestEntities(Entity entity) {
		if (new Random().nextInt(effectInt) == 0) {
			if (entity instanceof Player) {
				Player ePlayer = (Player) entity;
				BendingPlayer bEntity = BendingPlayer.getBendingPlayer(ePlayer);
				if (bEntity.hasElement(Element.getElement("DarkSpirit"))) {
					if (healDarkSpirits) {
						LivingEntity le = (LivingEntity)entity;
						le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 1));
						ParticleEffect.HEART.display(entity.getLocation().add(0, 2, 0), 0, 0, 0, 0, 1);
					}
				} else {
					DamageHandler.damageEntity(entity, damage, this);
					ParticleEffect.PORTAL.display(entity.getLocation().add(0, 1, 0), 0, 0, 0, 1.5F, 5);
				}
				
			} else if (entity instanceof Monster) {
				LivingEntity le = (LivingEntity)entity;
				le.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 1));
				ParticleEffect.ANGRY_VILLAGER.display(entity.getLocation().add(0, 1, 0), 0, 0, 0, 0, 1);
			} else if (entity instanceof LivingEntity && damageEntities) {
				DamageHandler.damageEntity(entity, damage, this);
				ParticleEffect.PORTAL.display(entity.getLocation().add(0, 1, 0), 0, 0, 0, 1.5F, 5);
				
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
		combo.add(new AbilityInformation("Intoxicate", ClickType.RIGHT_CLICK_BLOCK));
		combo.add(new AbilityInformation("Intoxicate", ClickType.SHIFT_UP));
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
		return Methods.setSpiritDescription(SpiritType.DARK, "Combo") + Main.plugin.getConfig().getString("Language.Abilities.DarkSpirit.Infest.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.setSpiritDescriptionColor(SpiritType.DARK) + Main.plugin.getConfig().getString("Langauge.Abilities.DarkSpirit.Infest.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.setSpiritDescriptionColor(SpiritType.DARK) + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.setSpiritDescriptionColor(SpiritType.DARK) + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return Main.plugin.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Combo.Infest.Enabled");
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
