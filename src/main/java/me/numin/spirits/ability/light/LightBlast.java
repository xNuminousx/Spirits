package me.numin.spirits.ability.light;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import me.numin.spirits.Methods;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.LightAbility;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LightBlast extends LightAbility implements AddonAbility {

    public enum Action {
        CLICK, SHIFT
    }
    public boolean canHeal = false;
    private Action action;

    private Entity entity;
    private Location location;
    private Location origin;

    private boolean hasSelected = false, defineLocations = true;
    private double range;
    private long cooldown;
    private long time;

    private Vector targetDirection = new Vector(1, 0, 0);
    private Location currentLocation, targetLocation;

    public LightBlast(Player player, Action action) {
        super(player);

        this.cooldown = 0;
        this.range = 20;
        this.action = action;
        this.location = player.getEyeLocation();
        this.origin = player.getLocation().add(0, 1, 0);
        this.time = System.currentTimeMillis();

        if (this.action == Action.SHIFT && entity == null) {
            Entity target = GeneralMethods.getTargetedEntity(player, range);
            //TODO: add armor stand check
            if (target instanceof LivingEntity) {
                this.entity = target;
                this.targetLocation = entity.getLocation();
                this.hasSelected = true;
            }
        }
        start();
    }

    @Override
    public void progress() {
        if (!player.isOnline() || player.isDead() || GeneralMethods.isRegionProtectedFromBuild(player, this.getLocation())) {
            remove();
            return;
        }
        switch (this.action) {
            case CLICK: this.onClick();
            case SHIFT: this.onShift();
        }
        if (canHeal) {
            this.animateHeal();
        }

        if (System.currentTimeMillis() > time + 10000) {
            remove();
        }
    }

    private void onClick() {
        this.animateBlast();
    }

    private void onShift() {
        if (this.hasSelected) {
            this.animateSelection();
        }
    }

    private void animateBlast() {
        Vector direction = location.getDirection();
        location.add(direction.multiply(1));
        player.spawnParticle(Particle.VILLAGER_HAPPY, location, 2, 0, 0, 0, 0);

        if (origin.distanceSquared(location) > range * range) {
            remove();
        }
    }

    private void animateHeal() {
        boolean hasReached = false;
        if (entity == null || entity.isDead() || entity.getWorld() != player.getWorld() || GeneralMethods.isRegionProtectedFromBuild(player, entity.getLocation())) {
            remove();
            return;
        }
        if (defineLocations) {
            currentLocation = player.getLocation().add(0, 1, 0);
            targetLocation = entity.getLocation();
            defineLocations = false;
        }
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(currentLocation, 2)) {
            if (e.equals(entity)) {
                hasReached = true;
                player.sendMessage("Has reached!");
            }
        }
        if (!hasReached) {
            targetLocation = entity.getLocation().add(0, 1, 0);
            targetDirection.add(targetLocation.toVector().subtract(currentLocation.toVector()).multiply(1)).normalize();
            currentLocation.add(targetDirection.clone().multiply(0.5));
            player.getWorld().spawnParticle(Particle.FLAME, currentLocation, 1, 0, 0, 0, 0);
        } else {
            //apply effects
            canHeal = false;
            remove();
        }
    }

    private int currPoint;
    private boolean canAnimate = true;
    private void animateSelection() {
        Location location = this.entity.getLocation();
        if (this.entity == null || this.entity.isDead() || this.entity.getWorld() != player.getWorld() || GeneralMethods.isRegionProtectedFromBuild(player, entity.getLocation())) {
            return;
        }
        if (!canAnimate) {
            return;
        }
        for (int i = 0; i < 1; i++) {
            currPoint += 360/180;
            if (currPoint == 180) {
                canAnimate = false;
            }
            double angle = currPoint * Math.PI / 90;
            double x = Math.cos(angle * 3);
            double y = Math.cos(angle);
            double z = Math.sin(angle * 3);
            location.add(x, y + 1, z);
            this.entity.getWorld().spawnParticle(Particle.FLAME, location, 1, 0, 0, 0, 0);
            location.subtract(x, y + 1, z);
        }
    }

    public Action getAction() {
        return this.action;
    }

    @Override public void remove() {
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
        return "LightBlast";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(Methods.SpiritType.LIGHT, "Offense / Utility") +
                Spirits.plugin.getConfig().getString("Language.Abilities.LightSpirit.LightBlast.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(Methods.SpiritType.LIGHT) +
                Spirits.plugin.getConfig().getString("Language.Abilities.LightSpirit.LightBlast.Instructions");
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
        switch (this.action) {
            case CLICK:
            case SHIFT:
        }
        return entity != null ? entity.getLocation() : player.getLocation();
    }

    @Override
    public void load() {}
    @Override
    public void stop() {}
}