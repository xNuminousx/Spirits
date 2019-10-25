package me.numin.spirits.ability.api;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import me.numin.spirits.utilities.SpiritElement;
import org.bukkit.entity.Player;

public abstract class DarkAbility extends ElementalAbility {

    public DarkAbility(Player player) {
        super(player);
    }

    @Override
    public Element getElement() {
        return SpiritElement.DARK_SPIRIT;
    }
}
