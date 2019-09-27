package me.numin.spirits.ability.light;

import me.numin.spirits.ability.api.removal.Removal;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.numin.spirits.Spirits;
import me.numin.spirits.Methods;
import me.numin.spirits.Methods.SpiritType;
import me.numin.spirits.ability.api.LightAbility;

public class Shelter extends LightAbility implements AddonAbility {

    public enum ShelterType {
        CLICK, SHIFT
    }
    private Entity target;
    private Location blast, location, origin;
    private Removal removal;
    private ShelterType shelterType;
    private Vector direction;

    private boolean moveBlast, removeIfFar, removeOnDamage;
    private double othersRadius, selfRadius, startHealth;
    private int currPoint, range, farAwayRange;
    private long duration, othersCooldown, selfCooldown, time;

    public Shelter(Player player, ShelterType shelterType) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        setFields();

        time = System.currentTimeMillis();
        this.shelterType = shelterType;
        startHealth = player.getHealth();
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.1F, 2);

        start();
    }

    private void setFields() {
        this.othersCooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Others.Cooldown");
        this.selfCooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Self.Cooldown");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Shelter.Duration");
        this.removeOnDamage = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirits.Shelter.RemoveOnDamage");
        this.range = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.Others.Range");
        this.removeIfFar = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Shelter.RemoveIfFarAway.Enabled");
        this.farAwayRange = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Shelter.RemoveIfFarAway.Range");
        this.othersRadius = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.LightSpirit.Shelter.Others.Radius");
        this.selfRadius = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.LightSpirit.Shelter.Self.Radius");

        this.origin = player.getLocation().clone().add(0, 1, 0);
        this.location = origin.clone();
        this.direction = player.getLocation().getDirection();
        this.moveBlast = true;
        this.removal = new Removal(player);
    }

    @Override
    public void progress() {
        if (removal.stop()) {
            remove();
            return;
        }
        if (this.shelterType == ShelterType.CLICK) shieldOther();
        else if (this.shelterType == ShelterType.SHIFT && player.isSneaking()) shieldSelf();
    }

    private void shieldSelf() {
        if (System.currentTimeMillis() > time + duration) {
            remove();
        } else {
            rotateShield(player.getLocation(), 96, selfRadius);
            for (Entity approachingEntity : GeneralMethods.getEntitiesAroundPoint(player.getLocation(), selfRadius)) {
                if (approachingEntity instanceof LivingEntity && !approachingEntity.getUniqueId().equals(player.getUniqueId())) {
                    this.blockEntity((LivingEntity)approachingEntity);
                } else if (approachingEntity instanceof Projectile) {
                    Projectile projectile = (Projectile)approachingEntity;
                    projectile.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, projectile.getLocation(), 20, 0, 0, 0, 0.09);
                    projectile.remove();
                }
            }
        }
    }

    private void shieldOther() {
        if (moveBlast) {
            blast = location.add(direction.multiply(1).normalize());
            progressBlast(blast);
            if (origin.distance(blast) > range) {
                remove();
                return;
            }
            for (Entity target : GeneralMethods.getEntitiesAroundPoint(blast, 2)) {
                if (target instanceof LivingEntity && !target.getUniqueId().equals(player.getUniqueId())) {
                    this.target = target;
                    this.time = System.currentTimeMillis();
                    this.moveBlast = false;
                }
            }
        } else {
            if (System.currentTimeMillis() > time + duration) {
                remove();
            } else {
                rotateShield(this.target.getLocation(), 100, othersRadius);
                if (removeIfFar && (player.getLocation().distance(target.getLocation()) > farAwayRange)) {
                    remove();
                    return;
                }
                if (removeOnDamage && (player.getHealth() <= startHealth)) {
                    remove();
                    return;
                }
                for (Entity approachingEntity : GeneralMethods.getEntitiesAroundPoint(this.target.getLocation(), othersRadius)) {
                    if (approachingEntity instanceof LivingEntity && !approachingEntity.getUniqueId().equals(this.target.getUniqueId()) && !approachingEntity.getUniqueId().equals(player.getUniqueId())) {
                        this.blockEntity((LivingEntity)approachingEntity);
                    } else if (approachingEntity instanceof Projectile) {
                        Projectile projectile = (Projectile)approachingEntity;
                        projectile.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, projectile.getLocation(), 20, 0, 0, 0, 0.09);
                        projectile.remove();
                    }
                }
            }
        }
    }

    private void rotateShield(Location location, int points, double size) {
        for (int t = 0; t < 6; t++) {
            currPoint += 360 / points;
            if (currPoint > 360) {
                currPoint = 0;
            }
            double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
            double x2 = size * Math.cos(angle);
            double y = 0.9 * (Math.PI * 5 - t) - 10;
            double z2 = size * Math.sin(angle);
            location.add(x2, y, z2);
            ParticleEffect.SPELL_INSTANT.display(location, 0.5F, 0.5F, 0.5F, 0, 1);
            location.subtract(x2, y, z2);
        }
    }
    private void progressBlast(Location location) {
        for (int i = 0; i < 6; i++) {
            currPoint += 360 / 100;
            if (currPoint > 360) {
                currPoint = 0;
            }
            double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
            double x = 0.04 * (Math.PI * 4 - angle) * Math.cos(angle + i);
            double z = 0.04 * (Math.PI * 4 - angle) * Math.sin(angle + i);
            location.add(x, 0.1F, z);
            ParticleEffect.SPELL_INSTANT.display(location, 0, 0, 0, 0, 1);
            location.subtract(x, 0.1F, z);
        }
    }

    private void blockEntity(LivingEntity entity) {
        Vector velocity = entity.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(0.1);
        velocity.setY(-0.5);
        entity.setVelocity(velocity);
    }

    @Override
    public void remove() {
        switch (this.shelterType) {
            case SHIFT: bPlayer.addCooldown(this, selfCooldown);
            break;
            case CLICK: bPlayer.addCooldown(this, othersCooldown);
            break;
        }
        super.remove();
    }

    @Override
    public long getCooldown() {
        return shelterType == ShelterType.CLICK ? othersCooldown : selfCooldown;
    }

    @Override
    public Location getLocation() {
        return shelterType == ShelterType.CLICK ? blast : player.getLocation();
    }

    @Override
    public double getCollisionRadius() {
        return this.shelterType == ShelterType.CLICK ? othersRadius : selfRadius;
    }

    @Override
    public String getName() {
        return "Shelter";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(SpiritType.LIGHT, "Defense") +
                Spirits.plugin.getConfig().getString("Language.Abilities.LightSpirit.Shelter.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(SpiritType.LIGHT) + Spirits.plugin.getConfig().getString("Language.Abilities.LightSpirit.Shelter.Instructions");
    }

    @Override
    public String getAuthor() {
        return Methods.getSpiritColor(SpiritType.LIGHT) + "" + Methods.getAuthor();
    }

    @Override
    public String getVersion() {
        return Methods.getSpiritColor(SpiritType.LIGHT) + Methods.getVersion();
    }

    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Shelter.Enabled");
    }

    @Override
    public boolean isExplosiveAbility() {
        return false;
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
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public void load() {}
    @Override
    public void stop() {}
}