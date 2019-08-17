package me.numin.spirits.command;

import com.projectkorra.projectkorra.command.PKCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpiritsCommand extends PKCommand {

    public SpiritsCommand() {
        super("spirits", "/bending spirits", "Opens up Spirits guide.", new String[] {"s", "sp", "spirits", "spirit"});
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            player.sendMessage("You're a player!");
        } else {
            commandSender.sendMessage("You're not a player!");
        }
    }
}
