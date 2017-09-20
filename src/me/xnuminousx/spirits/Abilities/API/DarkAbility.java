package me.xnuminousx.spirits.Abilities.API;

import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.projectkorra.projectkorra.ability.SubAbility;

import me.xnuminousx.spirits.Elements.SpiritElement;

public abstract class DarkAbility extends ElementalAbility implements SubAbility {

	public DarkAbility(Player player) {
		super(player);
	}
	
	@Override
	public Class<? extends Ability> getParentAbility() {
		return SpiritAbility.class;
	}
	
	@Override
	public Element getElement() {
		return SpiritElement.DARK_SPIRIT;
	}
}
