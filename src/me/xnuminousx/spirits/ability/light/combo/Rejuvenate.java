package me.xnuminousx.spirits.ability.light.combo;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
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
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.LightAbility;

public class Rejuvenate extends LightAbility implements AddonAbility, ComboAbility {

	private Location location;
	private Location location2;
	private Location circleCenter;
	private long time;
	private long cooldown;
	private long duration;
	private int radius;
	private int effectInt;
	private boolean damageMonsters;
	private boolean damageDarkSpirits;
	private double damage;
	private int currPoint;
	private Location location3;

	public Rejuvenate(Player player) {
		super(player);
		
		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}
		setFields();
		time = System.currentTimeMillis();
		start();
		bPlayer.addCooldown(this);
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Cooldown");
		this.duration = ConfigManager.getConfig().getLong("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Duration");
		this.radius = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Radius");
		this.effectInt = ConfigManager.getConfig().getInt("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.EffectInterval");
		this.damage = ConfigManager.getConfig().getDouble("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Damage");
		this.damageDarkSpirits = ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.HurtDarkSpirits");
		this.damageMonsters = ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.HurtMonsters");
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
		Methods.createPolygon(location, 8, radius, 0.2, ParticleEffect.INSTANT_SPELL);
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
			ParticleEffect.END_ROD.display(location2, 0, 0, 0, 0, 1);
			location2.subtract(x, 0, z);
			
			location3.add(x2, 0, z2);
			ParticleEffect.END_ROD.display(location3, 0, 0, 0, 0, 1);
			location3.subtract(x2, 0, z2);
		}
		
		ParticleEffect.ENCHANTMENT_TABLE.display(location, radius / 2, 0.4F, radius / 2, 0, 10);
	}
	
	public void grabEntities() {
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(circleCenter, radius)) {
			if (entity instanceof LivingEntity) {
				healEntities(entity);
			}
		}
	}
	
	public void healEntities(Entity entity) {
		if (new Random().nextInt(effectInt) == 0) {
			if (entity instanceof Player && damageDarkSpirits) {
				Player ePlayer = (Player) entity;
				BendingPlayer bEntity = BendingPlayer.getBendingPlayer(ePlayer);
				if (bEntity.hasElement(Element.getElement("DarkSpirit"))) {
					DamageHandler.damageEntity(ePlayer, damage, this);
				}
				
			} else if (entity instanceof Monster && damageMonsters) {
				DamageHandler.damageEntity(entity, damage, this);
				
			} else {
				LivingEntity le = (LivingEntity)entity;
				le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 1));
				ParticleEffect.HEART.display(entity.getLocation().add(0, 2, 0), 0, 0, 0, 0, 1);
			}
		}
	}

	@Override
	public Object createNewComboInstance(Player player) {
		return new Rejuvenate(player);
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		ArrayList<AbilityInformation> combo = new ArrayList<>();
		combo.add(new AbilityInformation("Alleviate", ClickType.SHIFT_DOWN));
		combo.add(new AbilityInformation("Alleviate", ClickType.RIGHT_CLICK_BLOCK));
		combo.add(new AbilityInformation("Alleviate", ClickType.SHIFT_UP));
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

		return "Rejuvenate";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("light", "Combo") + ConfigManager.languageConfig.get().getString("Abilities.LightSpirit.Rejuvenate.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.spiritChatColor("light") + ConfigManager.languageConfig.get().getString("Abilities.LightSpirit.Rejuvenate.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.spiritChatColor("light") + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.spiritChatColor("light") + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Enabled");
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
