package me.xnuminousx.spirits.ability.api;

import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ElementalAbility;

import me.xnuminousx.spirits.elements.SpiritElement;

public abstract class SpiritAbility extends ElementalAbility {
	
	public SpiritAbility(Player player) {
		super(player);
	}
	public final Element getElement() {
		return SpiritElement.SPIRIT;
		
	}
}
