package me.numin.spirits.command.configurations;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.ComboAbility;
import me.numin.spirits.Spirits;
import me.numin.spirits.utilities.SpiritElement;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

import java.util.Set;
import java.util.regex.Pattern;

class SpiritsConfigCommand {

    private Ability ability;
    private CommandSender commandSender;
    private String section = "";

    SpiritsConfigCommand(CommandSender commandSender, Ability ability) {
        this.ability = ability;
        this.commandSender = commandSender;

        if (commandSender == null)
            return;

        if (ability == null) {
            commandSender.sendMessage("That's not a valid ability.");
            return;
        }

        fetchValues();
    }

    private void fetchValues() {
        String elementName = getElementStringFromElement(ability.getElement());

        if (elementName == null) return;

        String path = ability instanceof ComboAbility ?
                "Abilities.Spirits." + elementName + ".Combo." + ability.getName() :
                "Abilities.Spirits." + elementName + "." + ability.getName();

        Set<String> keys = Spirits.plugin.getConfig().getKeys(true);

        commandSender.sendMessage(ability.getElement().getColor() + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + ability.getName());
        commandSender.sendMessage("");

        for (String key : keys) {
            if (key.contains(path)) {
                String[] parts = key.split(Pattern.quote("."));

                String valueType = parts[parts.length - 1];
                Object value = Spirits.plugin.getConfig().get(key);

                if (value == null || value instanceof MemorySection)
                    continue;

                if (parts.length >= 5) {
                    String sectionName = parts[parts.length - 2];

                    if (!section.equals(sectionName) && !sectionName.equals(ability.getName())) {
                        section = sectionName;
                        commandSender.sendMessage("");
                        commandSender.sendMessage(ability.getElement().getColor() + "" + ChatColor.BOLD + sectionName);
                    }
                }

                commandSender.sendMessage(ability.getElement().getColor() + valueType + ": " + value.toString());
            }
        }
    }

    private String getElementStringFromElement(Element element) {
        if (element.equals(SpiritElement.SPIRIT))
            return "Neutral";
        else if (element.equals(SpiritElement.LIGHT_SPIRIT))
            return "LightSpirit";
        else if (element.equals(SpiritElement.DARK_SPIRIT))
            return "DarkSpirit";
        else return null;
    }
}
