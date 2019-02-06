package me.xnuminousx.spirits.ability.spirit.combo;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.xnuminousx.spirits.Main;
import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.Methods.SpiritType;
import me.xnuminousx.spirits.ability.api.SpiritAbility;

public class Phase extends SpiritAbility implements AddonAbility, ComboAbility {

	private int range;
	private long cooldown;
	private long time;
	private long duration;
	private long vanishCD;
	private int minHealth;
	private boolean isPhased;
	private boolean playEffects;
	private boolean applyVanishCD;
	private Location origin;
	private GameMode originGM;
	private World playerWorld;

	public Phase(Player player) {
		super(player);
		
		if (!bPlayer.canBendIgnoreBinds(this)) {
			return;
		}
		
		setFields();
		isPhased = false;
		playEffects = true;
		time = System.currentTimeMillis();
		originGM = player.getGameMode();
		playerWorld = player.getWorld();
		start();
	}

	private void setFields() {
		this.cooldown = Main.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Phase.Cooldown");
		this.duration = Main.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Phase.Duration");
		this.range = Main.plugin.getConfig().getInt("Abilities.Spirits.Neutral.Combo.Phase.Range");
		this.minHealth = Main.plugin.getConfig().getInt("Abilities.Spirits.Neutral.Combo.Phase.MinHealth");
		this.applyVanishCD = Main.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Phase.Vanish.ApplyCooldown");
		this.vanishCD = Main.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Phase.Vanish.Cooldown");
		this.origin = player.getLocation();
	}

	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || (player.getWorld() != playerWorld) || GeneralMethods.isRegionProtectedFromBuild(this, origin)) {
			resetGameMode();
			remove();
			return;
		}
		if (player.getHealth() <= minHealth) {
			remove();
			return;
		}
		setGameMode();
		if (playEffects) {
			playEffects = false;
			playEffects();
		}
		if (System.currentTimeMillis() > time + duration || origin.distanceSquared(player.getLocation()) > range * range) {
			playEffects();
			resetGameMode();
			remove();
			return;
		}
		if (player.isSneaking() && isPhased) {
			playEffects();
			resetGameMode();
			remove();
			return;
		}
	}
	
	public void setGameMode() {
		player.setGameMode(GameMode.SPECTATOR);
		isPhased = true;
	}
	
	public void resetGameMode() {
		player.setGameMode(originGM);
		isPhased = false;
		bPlayer.addCooldown(this);
		if (applyVanishCD) {
			bPlayer.addCooldown("Vanish", vanishCD);
		}
	}
	
	public void playEffects() {
		ParticleEffect.PORTAL.display(player.getLocation().add(0, 1, 0), 100, 0, 0, 0, 1.5F);
		Methods.playSpiritParticles(bPlayer, player.getLocation().add(0, 1, 0), 1, 1, 1, 0, 20);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5F, -1);
	}

	@Override
	public Object createNewComboInstance(Player player) {
		return new Phase(player);
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		ArrayList<AbilityInformation> combo = new ArrayList<>();
		combo.add(new AbilityInformation("Vanish", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Vanish", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Possess", ClickType.SHIFT_DOWN));
		combo.add(new AbilityInformation("Possess", ClickType.SHIFT_UP));
		combo.add(new AbilityInformation("Vanish", ClickType.LEFT_CLICK));
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

		return "Phase";
	}
	
	@Override
	public String getDescription() {
		return Methods.setSpiritDescription(SpiritType.NEUTRAL, "Combo") + Main.plugin.getConfig().getString("Language.Abilities.Spirit.Phase.Description");
	}
	
	@Override
	public String getInstructions() {
		return Methods.setSpiritDescriptionColor(SpiritType.NEUTRAL) + Main.plugin.getConfig().getString("Language.Abilities.Spirit.Phase.Instructions");
	}

	@Override
	public String getAuthor() {

		return Methods.setSpiritDescriptionColor(SpiritType.NEUTRAL) + Methods.getAuthor();
	}

	@Override
	public String getVersion() {

		return Methods.setSpiritDescriptionColor(SpiritType.NEUTRAL) + Methods.getVersion();
	}
	
	@Override
	public boolean isEnabled() {
		return Main.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Phase.Enabled");
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
