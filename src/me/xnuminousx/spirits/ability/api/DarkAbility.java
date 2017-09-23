package me.xnuminousx.spirits.ability.api;

import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ElementalAbility;

import me.xnuminousx.spirits.elements.SpiritElement;

public abstract class DarkAbility extends ElementalAbility {

	public DarkAbility(Player player) {
		super(player);
	}
	
	@Override
	public Element getElement() {
		return SpiritElement.DARK_SPIRIT;
	}
}
