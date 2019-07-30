package me.numin.spirits.ability.api;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import me.numin.spirits.SpiritElement;
import org.bukkit.entity.Player;

public abstract class LightAbility extends ElementalAbility {

    public LightAbility(Player player) {
        super(player);
    }

    @Override
    public Element getElement() {
        return SpiritElement.LIGHT_SPIRIT;
    }
}
