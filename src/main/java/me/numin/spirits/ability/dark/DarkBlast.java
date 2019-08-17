package me.numin.spirits.ability.dark;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import me.numin.spirits.Methods;
import me.numin.spirits.ability.api.DarkAbility;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class DarkBlast extends DarkAbility implements AddonAbility {

    public enum Type {
        SHIFT, CLICK
    }

    //General Variables
    private DustOptions blue = new DustOptions(Color.fromRGB(0, 255, 247), 1);
    private DustOptions white = new DustOptions(Color.fromRGB(255, 255, 255), 1);
    private Location blastLoc;
    private Type action;
    private double range;
    private int point;
    private long cooldown;

    //Selection Variables
    private Entity target;
    private Location playerOrigin, playerLocation, targetLocation;
    private Vector vector = new Vector(1, 0, 0);
    public boolean canShoot = false, hasReached;
    private boolean canSpiral = true, hasSight, hasSelected, setupLocation = true;

    //Blast Variables
    private Vector direction;
    private Location location, origin;
    private boolean hasShot = false;

    public DarkBlast(Player player, Type action) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        this.action = action;
        this.cooldown = 1000;
        this.range = 20;

        if (this.action == Type.SHIFT) {
            this.target = GeneralMethods.getTargetedEntity(player, range);
            this.playerOrigin = player.getLocation().add(0, 1, 0);
            this.playerLocation = this.playerOrigin;

            if (target != null) {
                this.targetLocation = target.getLocation().add(0, 1, 0);
                this.hasReached = false;
                this.hasSelected = true;
            }

        } else {
            this.origin = player.getLocation().add(0, 1, 0);
            this.location = GeneralMethods.getTargetedLocation(player, 1);
            this.direction = this.location.getDirection().normalize();
            this.hasShot = true;
        }
        start();
    }

    @Override
    public void progress() {
        if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(player, player.getLocation())) {
            remove();
            return;
        }

        switch(this.action) {
            case CLICK: this.progressBlast();
            case SHIFT: this.progressSelection();
        }
    }

    private void progressBlast() {
        this.blastLoc = this.advanceLocationToDirection(this.direction, this.location);

        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, blastLoc,
                1, 0, 0, 0, 0);

        if (origin.distanceSquared(blastLoc) > range * range) {
            remove();
        }
    }
    List<Location> locations;
    private boolean set = true;
    private void progressSelection() {
        /*if (target == null) {
            return;
        }*/

        if (!player.isSneaking()) {
            remove();
            return;
        }

        /*for (int i = 1; i<= 6; i++) {
            Random rand = new Random();
            int x = rand.nextInt(3), y = rand.nextInt(3), z = rand.nextInt(3);
            Location loc = player.getLocation().add(x, y, z);
            locations.add(loc);
        }
        for (Location location : locations) {
            Location newLoc = this.advanceLocationToPoint(vector, location, player.getLocation());
            player.getWorld().spawnParticle(Particle.FLAME, newLoc,
                    1, 0, 0, 0, 0);
        }*/



        /*if (canSpiral && !hasReached) {
            this.moveSpiral();
        }

        if (canShoot) {
            if (!hasReached) {
                if (setupLocation) {
                    this.playerLocation = player.getLocation().add(0, 1, 0);
                    this.setupLocation = false;
                }
                this.blastLoc = this.advanceLocationToPoint(vector, playerLocation, target.getLocation());

                if (playerOrigin.distance(blastLoc) > range) {
                    remove();
                    return;
                }

                player.getWorld().spawnParticle(Particle.FLAME, blastLoc,
                        1, 0, 0, 0, 0);
            } else {
                remove();
                return;
            }

            for (Entity entity : GeneralMethods.getEntitiesAroundPoint(blastLoc, 1.5)) {
                if (entity.equals(target)) {
                    this.hasReached = true;
                    this.canSpiral = false;
                }
            }
        }*/
    }

    //TODO: remove these methods
    private Location advanceLocationToDirection(Vector direction, Location origin) {
        origin.add(direction.multiply(1).normalize().clone());
        return origin;
    }

    private Location advanceLocationToPoint(Vector vector, Location point1, Location point2) {
        vector.add(point2.toVector()).subtract(point1.toVector()).multiply(1).normalize();
        point1.add(vector.clone().multiply(0.5));
        return point1;
    }

    private void moveSpiral() {
        Location spiral1 = target.getLocation();
        Location spiral2 = target.getLocation();
        for (int i = 0; i <= 1; i++) {
            point += 360/180;
            if (point == 180) {
                canSpiral = false;
            }
            double angle = point * Math.PI / 180;
            double x = Math.cos(angle * 5);
            double y = Math.cos(angle * 2.2);
            double z = Math.sin(angle * 5);
            spiral1.add(x, y + 1, z);
            spiral2.add(-x, y + 1, -z);
            this.target.getWorld().spawnParticle(Particle.REDSTONE, spiral1, 1, 0, 0, 0, blue);
            this.target.getWorld().spawnParticle(Particle.REDSTONE, spiral1, 1, 0, 0, 0, white);
            this.target.getWorld().spawnParticle(Particle.REDSTONE, spiral2, 1, 0, 0, 0, blue);
            this.target.getWorld().spawnParticle(Particle.REDSTONE, spiral2, 1, 0, 0, 0, white);
            spiral1.subtract(x, y + 1, z);
            spiral2.subtract(-x, y + 1, -z);
        }
    }

    public Type getAction() {
        return this.action;
    }

    @Override
    public void remove() {
        bPlayer.addCooldown(this);
        super.remove();
    }

    @Override
    public boolean isSneakAbility() {
        return true;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public boolean isIgniteAbility() {
        return false;
    }

    @Override
    public boolean isExplosiveAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public String getName() {
        return "DarkBlast";
    }

    @Override
    public String getAuthor() {
        return Methods.getSpiritColor(Methods.SpiritType.LIGHT) + "" + Methods.getAuthor();
    }

    @Override
    public String getVersion() {
        return Methods.getSpiritColor(Methods.SpiritType.LIGHT) + Methods.getVersion();
    }

    @Override
    public Location getLocation() {
        if (this.action == null) {
            return player.getLocation();
        }
        switch (this.action) {
            case CLICK: return hasShot ? blastLoc : player.getLocation();
            case SHIFT: return hasSelected && target != null ? targetLocation : playerLocation;
            default: return player.getLocation();
        }
    }

    @Override
    public void load() {}
    @Override
    public void stop() {}
}