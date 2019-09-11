package me.numin.spirits;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.CoreAbility;
import me.numin.spirits.command.SpiritsCommand;
import me.numin.spirits.config.Config;
import me.numin.spirits.listeners.Abilities;
import me.numin.spirits.listeners.Passives;
import me.numin.spirits.listeners.PkEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spirits extends JavaPlugin {

    public static Spirits plugin;

    @Override
    public void onEnable() {
        plugin = this;

        new Config(this);
        new SpiritsCommand();
        new SpiritElement();

        CoreAbility.registerPluginAbilities(plugin, "me.numin.spirits.ability");
        this.registerListeners();
        this.registerPermissions();

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

    private void registerPermissions() {
        // Ability Permissions
        String[] abilities = {"Infest", "Intoxicate", "Shackle", "Strike",
        "Rejuvenate", "Alleviate", "Orb", "Shelter", "LightBlast",
        "Phase", "Agility", "Vanish", "Possess", "DarkBlast", "Levitation"};
        for (String ability : abilities) {
            CoreAbility coreAbility = CoreAbility.getAbility(ability);
            String node = "bending.ability." + ability.toLowerCase();
            if (coreAbility == null || !coreAbility.isEnabled() ||
                    ProjectKorra.plugin.getServer().getPluginManager().getPermission(node) == null)
                return;
            Permission perm = new Permission(node);
            perm.setDefault(PermissionDefault.TRUE);
            ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
        }
        // Choose Element Permissions
        String[] elements = {"Spirit", "LightSpirit", "DarkSpirit"};
        for (String ele : elements) {
            Element element = Element.getElement(ele);
            String node = "bending.command.choose." + ele.toLowerCase();
            if (element == null ||
                    ProjectKorra.plugin.getServer().getPluginManager().getPermission(node) == null) return;
            Permission perm = new Permission(node);
            perm.setDefault(PermissionDefault.TRUE);
            ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);
        }
    }
}
