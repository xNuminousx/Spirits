package me.xnuminousx.spirits.elements;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.ProjectKorra;

public class SpiritElement {
	
	public static final Element SPIRIT = new Element("Spirit", null, ProjectKorra.plugin);
	public static final Element LIGHT_SPIRIT = new Element("LightSpirit", null, ProjectKorra.plugin);
	public static final Element DARK_SPIRIT = new Element("DarkSpirit", null, ProjectKorra.plugin);
	
	public static final SubElement ENERGY= new SubElement("Energy", Element.AVATAR, null, ProjectKorra.plugin);
	public static final Element DARK_AVATAR = new Element("DarkAvatar", null, ProjectKorra.plugin);
}
