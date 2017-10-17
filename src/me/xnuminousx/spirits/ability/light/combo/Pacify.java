package me.xnuminousx.spirits.ability.light.combo;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.util.ClickType;

import me.xnuminousx.spirits.Methods;
import me.xnuminousx.spirits.ability.api.LightAbility;
import net.md_5.bungee.api.ChatColor;

public class Pacify extends LightAbility implements AddonAbility, ComboAbility {

	private long cooldown;

	public Pacify(Player player) {
		super(player);
	}

	@Override
	public void progress() {

	}

	@Override
	public Object createNewComboInstance(Player player) {
		return new Pacify(player);
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		ArrayList<AbilityInformation> combo = new ArrayList<>();
		combo.add(new AbilityInformation("Alleviate", ClickType.SHIFT_DOWN));
		combo.add(new AbilityInformation("Alleviate", ClickType.SHIFT_UP));
		combo.add(new AbilityInformation("Shelter", ClickType.LEFT_CLICK));
		combo.add(new AbilityInformation("Shelter", ClickType.SHIFT_DOWN));
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
		return "Pacify";
	}
	
	@Override
	public String getDescription() {
		return Methods.getSpiritDescription("Light", "Combo", "Nothing yet");
	}
	
	@Override
	public String getInstructions() {
		return ChatColor.AQUA + "Alleviate (Tap shift) > Shelter (Left-click) > Shelter (Hold shift)";
	}

	@Override
	public String getAuthor() {
		return ChatColor.AQUA + "xNuminousx";
	}

	@Override
	public String getVersion() {
		return ChatColor.AQUA + "1.0";
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
