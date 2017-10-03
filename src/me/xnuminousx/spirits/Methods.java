package me.xnuminousx.spirits;

import org.bukkit.Location;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class Methods {
	public static void getSpiritType(BendingPlayer bPlayer, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		Element ls = Element.getElement("LightSpirit");
		Element ds = Element.getElement("DarkSpirit");
		Element s = Element.getElement("Spirit");
		if (bPlayer.hasElement(ls) && bPlayer.hasElement(ds)) {
			ParticleEffect.MAGIC_CRIT.display(loc, offsetX, offsetY, offsetZ, speed, amount);
		
		} else if (bPlayer.hasElement(ds)) {
			ParticleEffect.WITCH_MAGIC.display(loc, offsetX, offsetY, offsetZ, speed, amount);
			
		} else if (bPlayer.hasElement(ls)) {
			ParticleEffect.INSTANT_SPELL.display(loc, offsetX, offsetY, offsetZ, speed, amount);
			
		} else if (!bPlayer.hasElement(ls) && !bPlayer.hasElement(ds) && bPlayer.hasElement(s)) {
			ParticleEffect.MAGIC_CRIT.display(loc, offsetX, offsetY, offsetZ, speed, amount);
		}
		return;
	}
}
