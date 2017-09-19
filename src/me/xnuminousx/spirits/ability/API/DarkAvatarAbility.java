package me.xnuminousx.spirits.ability.API;

import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;

import me.xnuminousx.spirits.Elements.SpiritElement;

public abstract class DarkAvatarAbility extends CoreAbility {

	public DarkAvatarAbility(Player player) {
		super(player);
		
	}
	public final Element getElement() {
		return SpiritElement.DARK_AVATAR;
		
	}

}
