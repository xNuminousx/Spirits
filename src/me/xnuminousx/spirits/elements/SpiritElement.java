package me.xnuminousx.spirits.elements;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;

import me.xnuminousx.spirits.Main;

public class SpiritElement {
	
	public static final Element SPIRIT = new Element("Spirit", null, Main.plugin);
	public static final SubElement LIGHT_SPIRIT = new SubElement("LightSpirit", SpiritElement.SPIRIT, null, Main.plugin);
	public static final SubElement DARK_SPIRIT = new SubElement("DarkSpirit", SpiritElement.SPIRIT, null, Main.plugin);
	public static final SubElement ENERGY= new SubElement("Energy", Element.AVATAR, null, Main.plugin);
	public static final Element DARK_AVATAR = new Element("DarkAvatar", null, Main.plugin);
}
