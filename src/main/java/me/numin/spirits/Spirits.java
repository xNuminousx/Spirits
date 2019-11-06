package me.numin.spirits;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.CoreAbility;
import me.numin.spirits.ability.light.Shelter;
import me.numin.spirits.command.configurations.ConfigCommand;
import me.numin.spirits.command.SpiritsCommand;
import me.numin.spirits.config.Config;
import me.numin.spirits.listeners.Abilities;
import me.numin.spirits.listeners.Passives;
import me.numin.spirits.listeners.PkEvent;
import me.numin.spirits.utilities.SpiritElement;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spirits extends JavaPlugin {

    public static Spirits plugin;

    @Override
    public void onEnable() {
        plugin = this;

        new Config(this);
        new SpiritsCommand();
        new ConfigCommand();
        new SpiritElement();

        CoreAbility.registerPluginAbilities(plugin, "me.numin.spirits.ability");

        registerListeners();

        //TODO: Probably doesn't actually do anything, needs testing.
        ProjectKorra.getCollisionInitializer().addLargeAbility(CoreAbility.getAbility(Shelter.class));

        plugin.getLogger().info("Successfully enabled Spirits.");
    }

    @Override
    public void onDisable() {
        plugin.getLogger().info("Successfully disabled Spirits.");
    }

    public static Spirits getInstance() {
        return plugin;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new Abilities(), this);
        getServer().getPluginManager().registerEvents(new Passives(), this);
        getServer().getPluginManager().registerEvents(new PkEvent(), this);
    }
}
