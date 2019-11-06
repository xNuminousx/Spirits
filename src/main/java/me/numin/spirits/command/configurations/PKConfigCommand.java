package me.numin.spirits.command.configurations;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.Ability;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

import java.util.Set;
import java.util.regex.Pattern;

class PKConfigCommand {

    private Ability ability;
    private CommandSender commandSender;
    private String section = "";

    PKConfigCommand(CommandSender commandSender, Ability ability) {
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
        Element element = ability.getElement();
        String elementName = element instanceof Element.SubElement ?
                ((Element.SubElement)element).getParentElement().getName() :
                element.getName();

        String path = "Abilities." + elementName + "." + ability.getName();

        Set<String> keys = ProjectKorra.plugin.getConfig().getKeys(true);

        commandSender.sendMessage(ability.getElement().getColor() + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + ability.getName());
        commandSender.sendMessage("");

        for (String key : keys) {
            if (key.contains(path)) {
                String[] parts = key.split(Pattern.quote("."));

                String valueType = parts[parts.length - 1];
                Object value = ProjectKorra.plugin.getConfig().get(key);

                if (value == null || value instanceof MemorySection)
                    continue;

                if (parts.length >= 5) {
                    String sectionName = parts[parts.length - 2];

                    if (!section.equals(sectionName)) {
                        section = sectionName;
                        commandSender.sendMessage("");
                        commandSender.sendMessage(ability.getElement().getColor() + "" + ChatColor.BOLD + sectionName);
                    }
                }

                commandSender.sendMessage(ability.getElement().getColor() + valueType + ": " + value.toString());
            }
        }
    }
}
