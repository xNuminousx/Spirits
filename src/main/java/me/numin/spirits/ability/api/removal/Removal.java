package me.numin.spirits.ability.api.removal;

import com.projectkorra.projectkorra.GeneralMethods;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Removal {

    private Entity entity;
    private Player player;
    private World world, entityWorld;
    private boolean requireSneak;

    public Removal(Player player) {
        this.player = player;
        this.requireSneak = false;
        this.world = player.getWorld();
    }

    public Removal(Player player, boolean requireSneak) {
        this.player = player;
        this.requireSneak = requireSneak;
        this.world = player.getWorld();
    }

    public Removal(Player player, boolean requireSneak, Entity entity) {
        this.entity = entity;
        this.entityWorld = entity.getWorld();
        this.player = player;
        this.requireSneak = requireSneak;
        this.world = player.getWorld();
    }

    /**
     * The method abilities should use to test
     * their validity.
     *
     * @return true If the ability should stop.
     */
    public boolean stop() {
        if (entity != null) return entityPass();
        if (requireSneak) return !player.isSneaking();
        return playerPass();
    }

    private boolean playerPass() {
        return player.isDead() ||
                !player.isOnline() ||
                world != player.getWorld() ||
                GeneralMethods.isRegionProtectedFromBuild(player, player.getLocation());
    }

    private boolean entityPass() {
        return entity.isDead() ||
                (entity instanceof Player && !((Player)entity).isOnline()) ||
                entityWorld != entity.getWorld() ||
                GeneralMethods.isRegionProtectedFromBuild(player, entity.getLocation());
    }
}