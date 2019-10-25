package me.numin.spirits.command;

import com.projectkorra.projectkorra.command.PKCommand;
import me.numin.spirits.Spirits;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpiritsCommand extends PKCommand {

    public SpiritsCommand() {
        super("spirits", "/bending spirits", "Opens up Spirits guide.", new String[] {"s", "sp", "spirit", "spirits"});
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;

            /*
            Example args: /b spirits getConfig abilities.spirits.lightspirits.orb.damage
            'b' and 'spirits' are not args
            'getConfig' and the config path are 2 args.
            'getConfig' is arg 0
            path is arg 1
             */

            if (list.size() == 2) {
                if (list.get(0).equalsIgnoreCase("get")) {
                    String configPath = list.get(1);
                    Object targetValue = Spirits.plugin.getConfig().get(configPath);
                    player.sendMessage(String.valueOf(targetValue));
                }
            }
        } else {
            commandSender.sendMessage("You're not a player!");
        }
    }
}
