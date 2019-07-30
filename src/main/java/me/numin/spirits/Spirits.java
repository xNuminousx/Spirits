package me.numin.spirits;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.CoreAbility;
import me.numin.spirits.config.Config;
import me.numin.spirits.listeners.Abilities;
import me.numin.spirits.listeners.Passives;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spirits extends JavaPlugin {

    public static Spirits plugin;

    @Override
    public void onEnable() {
        plugin = this;

        new Config(this);
        new SpiritElement();

        CoreAbility.registerPluginAbilities(plugin, "me.numin.spirits.ability");
        this.registerPermissions();
        this.registerListeners();

        plugin.getLogger().info("Successfully enabled Spirits.");
    }

    @Override
    public void onDisable() {
        plugin.getLogger().info("Successfully disabled Spirits.");
    }

    public static Spirits getInstance() {
        return plugin;
    }

    private void registerPermissions() {
        String[] abilities = {"Infest", "Intoxicate", "Shackle", "Strike",
        "Rejuvenate", "Alleviate", "Orb", "Shelter",
        "Phase", "Agility", "Vanish", "Possess"};

        for (String ability : abilities) {
            CoreAbility coreAbility = CoreAbility.getAbility(ability);

            if (coreAbility == null) return;

            if (coreAbility.isEnabled()) {
                if (ProjectKorra.plugin.getServer().getPluginManager().getPermission("bending.ability." + ability.toLowerCase()) == null) {
                    Permission perm = new Permission("bending.ability." + ability.toLowerCase());
                    perm.setDefault(PermissionDefault.TRUE);
                    ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
                }
            }
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new Abilities(), this);
        getServer().getPluginManager().registerEvents(new Passives(), this);
    }
}
