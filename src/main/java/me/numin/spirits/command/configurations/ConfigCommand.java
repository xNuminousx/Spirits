package me.numin.spirits.command.configurations;

import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.command.PKCommand;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ConfigCommand extends PKCommand {

    private List<String> pkAliases = Arrays.asList("projectkorra", "pk", "korra");
    private List<String> spiritAliases = Arrays.asList("spirits", "sp", "s");

    public ConfigCommand() {
        super("config", "/bending config <Plugin> <Ability>", "Shows the config values for an ability.", new String[] {"con", "config", "configs", "getconfig"});
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list) {
        if (commandSender == null || list == null) return;

        if (!commandSender.hasPermission("bending.command.getconfig")) {
            commandSender.sendMessage("You do not have permission for this command.");
            return;
        }

        if (list.size() <= 1) {
            commandSender.sendMessage("Invalid plugin format.");
            commandSender.sendMessage("Format: /b getconfig <plugin> <ability>");
            commandSender.sendMessage("");
            commandSender.sendMessage("Plugins & Aliases:");
            commandSender.sendMessage("ProjectKorra: projectkorra, pk, korra");
            commandSender.sendMessage("Spirits: spirits, sp, s");
            return;
        } else if (list.get(1) == null) {
            commandSender.sendMessage("That's not a valid ability.");
            return;
        }

        String pluginFrom = list.get(0);

        if (pkAliases.contains(pluginFrom)) {
            Ability ability = CoreAbility.getAbility(list.get(1));
            new PKConfigCommand(commandSender, ability);

        } else if (spiritAliases.contains(pluginFrom)) {
            Ability ability = CoreAbility.getAbility(list.get(1));
            new SpiritsConfigCommand(commandSender, ability);

        }
    }
}
