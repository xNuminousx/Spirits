package me.xnuminousx.spirits.Abilities.Avatar;

import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ElementalAbility;

import me.xnuminousx.spirits.Elements.SpiritElement;

public abstract class DarkAvatarAbility extends ElementalAbility {

	public DarkAvatarAbility(Player player) {
		super(player);
		
	}
	public final Element getElement() {
		return SpiritElement.DARK_AVATAR;
		
	}

}
