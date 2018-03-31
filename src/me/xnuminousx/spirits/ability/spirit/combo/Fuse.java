package me.xnuminousx.spirits.ability.spirit.combo;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.avatar.AvatarState;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class Fuse extends SpiritAbility implements AddonAbility, ComboAbility {
	
	private int potDuration;
	private long cooldown;
	private long time;
	private long maxDuration;
	private long dangerDelay;
	private boolean enableNPCMerge;
	private boolean isFusing;
	private boolean canCancel;
	private boolean killAfterDuration;
	private boolean enableAvatarState;
	private LivingEntity target;
	private Location origin;
	private GameMode originGM;

	public Fuse(Player player) {
		super(player);

		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}
		
		if (enableAvatarState) {
			new AvatarState((Player)target);
		}
		
		setFields();
		time = System.currentTimeMillis();
		start();
		bPlayer.addCooldown(this);
	}

	private void setFields() {
		this.cooldown = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Fuse.Cooldown");
		this.maxDuration = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Fuse.Duration");
		this.killAfterDuration = ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Fuse.Players.KillAfterDuration");
		this.dangerDelay = ConfigManager.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Fuse.NonPlayers.HarmfulFuseDelay");
		this.enableNPCMerge = ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Fuse.NonPlayers.Enabled");
		this.potDuration = ConfigManager.getConfig().getInt("Abilities.Spirits.Neutral.Combo.Fuse.NonPlayers.BuffDuration");
		this.origin = player.getLocation();
		this.originGM = player.getGameMode();
		isFusing = false;
		canCancel = false;
		enableAvatarState = false;
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, origin)) {
			defuse();
			remove();
			return;
		}
		dash();
		if (System.currentTimeMillis() > time + maxDuration) {
			ParticleEffect.FIREWORKS_SPARK.display(player.getLocation(), 0.5F, 0.5F, 0.5F, 0.5F, 20);
			ParticleEffect.DRAGON_BREATH.display(player.getLocation(), 0.5F, 0.5F, 0.5F, 1F, 20);
			if (target instanceof Player && killAfterDuration) {
				DamageHandler.damageEntity(target, 30, this);
			}
			defuse();
			remove();
			return;
		} else if (System.currentTimeMillis() > time + 1000) {
			canCancel = true;
		}
		if (player.isSneaking() && isFusing && canCancel) {
			defuse();
			remove();
			return;
		}
		
	}
	
	public void dash() {
		for (Entity target : GeneralMethods.getEntitiesAroundPoint(player.getLocation(), 1)) {
			if (((target instanceof Player)) && (target.getEntityId() != player.getEntityId())) {
				this.target = (LivingEntity) target;
				fuse(target);
			} else if (target instanceof LivingEntity && enableNPCMerge && (target.getEntityId() != player.getEntityId())) {
				this.target = (LivingEntity) target;
				fuse(target);
			}
		}
	}
	
	public void fuse(Entity target) {
		isFusing = true;
		player.setGameMode(GameMode.SPECTATOR);
		player.setSpectatorTarget(target);
		if (new Random().nextInt(5) == 0) {
			Methods.playSpiritParticles(bPlayer, player.getLocation().add(0, 1, 0), 0.5F, 0.5F, 0.5F, 0.2F, 5);
		}
		if (target instanceof Player) {
			enableAvatarState = true;
			if (System.currentTimeMillis() > time + dangerDelay) {
				if (new Random().nextInt(10) == 0) {
					DamageHandler.damageEntity(target, 1, this);
					ParticleEffect.MAGIC_CRIT.display(target.getLocation().add(0, 1, 0), 0, 0, 0, 0.2F, 10);
				}
			}
		} else if (target instanceof LivingEntity) {
			if (!enableNPCMerge) {
				defuse();
				remove();
				return;
			}
			if (System.currentTimeMillis() > time + dangerDelay) {
				if (new Random().nextInt(10) == 0) {
					DamageHandler.damageEntity(target, 1, this);
					ParticleEffect.MAGIC_CRIT.display(target.getLocation().add(0, 1, 0), 0, 0, 0, 0.2F, 10);
				}
			}
			LivingEntity le = (LivingEntity)target;
			le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, potDuration * 24, 2));
			le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, potDuration * 24, 1));
			le.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, potDuration * 24, 2));
		}
	}
	
	public void defuse() {
		if (player.getGameMode() == GameMode.SPECTATOR) {
			player.setSpectatorTarget(null);
			player.setGameMode(originGM);
		}
		
		if (target instanceof Player) {
			BendingPlayer bTarget = BendingPlayer.getBendingPlayer((Player)target);
			if (bTarget.isAvatarState()) {
				AvatarState avatarState = getAbility((Player)target, AvatarState.class);
				avatarState.remove();
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
		combo.add(new AbilityInformation("Possess", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Agility", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Agility", ClickType.SHIFT_DOWN));
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
		return Methods.getSpiritDescription("spirit", "Combo") +
				ConfigManager.languageConfig.get().getString("Abilities.Spirit.Fuse.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.spiritChatColor("spirit") + ConfigManager.languageConfig.get().getString("Abilities.Spirit.Fuse.Instructions");
	}

	@Override
	public String getAuthor() {
		return Methods.spiritChatColor("spirit") + Methods.getAuthor();
	}

	@Override
	public String getVersion() {
		return Methods.spiritChatColor("spirit") + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return ConfigManager.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Fuse.Enabled");
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
