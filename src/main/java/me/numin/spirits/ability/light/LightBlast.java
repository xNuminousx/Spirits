package me.numin.spirits.ability.light;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import me.numin.spirits.utilities.Methods;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.LightAbility;
import me.numin.spirits.utilities.Removal;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class LightBlast extends LightAbility implements AddonAbility {

    //TODO: Implement configuration.

    private Particle.DustOptions white = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1);
    private Particle.DustOptions pink = new Particle.DustOptions(Color.fromRGB(255, 160, 160), 1);
    private Entity target;
    private LightBlastType type;
    private Location blast, location, origin;
    private Removal removal;
    private Vector direction, vector;

    private boolean burst = true, canHeal, controllable, hasReached = false;
    private double damage, initialBlastSpeed, blastRadius, finalBlastSpeed, range;
    private int potionDuration, potionPower;
    private long cooldown, selectionDuration, time;

    public enum LightBlastType {
        SHIFT, CLICK
    }

    public LightBlast(Player player, LightBlastType type) {
        super(player);

        if (!bPlayer.canBend(this)) return;

        if (type != null) this.type = type;

        if (hasAbility(player, LightBlast.class) && type == LightBlastType.SHIFT) {
            LightBlast lightBlast = getAbility(player, LightBlast.class);
            if (lightBlast.target != null) {
                // Makes sure the player is looking at their target.
                Entity targetEntity = GeneralMethods.getTargetedEntity(player, lightBlast.range);
                if (targetEntity == null || !targetEntity.equals(lightBlast.target)) return;

                lightBlast.location = player.getLocation().add(0, 1, 0);
                lightBlast.canHeal = true;
            }
        } else {
            setFields();
            start();
            time = System.currentTimeMillis();
        }
    }

    private void setFields() {
        this.cooldown = 0;
        this.controllable = false;
        this.damage = 2;
        this.range = 10;
        this.selectionDuration = 2000;
        // heals 4 hearts
        this.potionDuration = 10;
        this.potionPower = 1;
        this.initialBlastSpeed = 1;
        this.blastRadius = 2;
        this.finalBlastSpeed = 0.2;

        this.direction = player.getLocation().getDirection();
        this.origin = player.getLocation().add(0, 1, 0);
        this.location = origin.clone();

        this.removal = new Removal(player);
        this.vector = new Vector(1, 0, 0);

        this.canHeal = false;
    }

    @Override
    public void progress() {
        if (this.removal.stop()) {
            remove();
            return;
        }

        if (type == LightBlastType.CLICK)
            shootDamagingBlast();
        else if (type == LightBlastType.SHIFT)
            shootSelectionBlast();

        showSelectedTarget();

        if (canHeal)
            shootHomingBlast();
    }

    private void shootDamagingBlast() {
        if (controllable)
            this.direction = player.getLocation().getDirection();

        this.blast = Methods.advanceLocationToDirection(direction, location, this.initialBlastSpeed);

        genericBlast(blast, false);

        if (origin.distance(blast) > range || GeneralMethods.isSolid(blast.getBlock()) || blast.getBlock().isLiquid()) {
            remove();
            return;
        }

        for (Entity target : GeneralMethods.getEntitiesAroundPoint(blast, this.blastRadius)) {
            if (target instanceof LivingEntity && !target.getUniqueId().equals(player.getUniqueId()) &&
                    !(target instanceof ArmorStand)) {
                DamageHandler.damageEntity(target, this.damage, this);
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, target.getLocation().add(0, 1, 0), 10, 0, 0, 0, 0.2);
                remove();
            }
        }
    }

    private void shootSelectionBlast() {
        if (target == null) {
            if (controllable)
                this.direction = player.getLocation().getDirection();

            this.blast = Methods.advanceLocationToDirection(direction, location, this.initialBlastSpeed);

            genericBlast(blast, true);

            if (origin.distance(blast) > range || GeneralMethods.isSolid(blast.getBlock()) || blast.getBlock().isLiquid()) {
                remove();
                return;
            }

            for (Entity target : GeneralMethods.getEntitiesAroundPoint(blast, this.blastRadius)) {
                if (target instanceof LivingEntity && !target.getUniqueId().equals(player.getUniqueId())) {
                    this.target = target;
                }
            }
        } else {
            if (player.getLocation().distance(target.getLocation()) > this.range ||
                    (System.currentTimeMillis() > time + selectionDuration && !canHeal)) {
                remove();
            }
        }
    }

    private void shootHomingBlast() {
        if (!hasReached) {
            this.blast = Methods.advanceLocationToPoint(vector, location, target.getLocation().add(0, 1,0), this.finalBlastSpeed);

            player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.1, 0.1, 0.1, 0, pink);

            if (player.getLocation().distance(target.getLocation()) > this.range ||
                    origin.distance(target.getLocation()) > this.range ||
                    GeneralMethods.isSolid(blast.getBlock()) || blast.getBlock().isLiquid() ||
                    !player.isSneaking()) {
                remove();
                return;
            }

            for (Entity entity : GeneralMethods.getEntitiesAroundPoint(blast, blastRadius)) {
                if (target.getUniqueId().equals(entity.getUniqueId())) {
                    hasReached = true;
                }
            }
        } else {
            this.healEntity(target);
        }
    }

    private void healEntity(Entity entity) {
        if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
            LivingEntity livingEntity = (LivingEntity)entity;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                    20 * this.potionDuration, this.potionPower, false, true, false));
            remove();
        }
    }

    private void genericBlast(Location location, boolean healing) {
        player.getWorld().spawnParticle(Particle.END_ROD, location, 2, 0.1, 0.1, 0.1, 0);
        player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.2, 0.2, 0.2, 0, white);

        if (healing)
            player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.2, 0.2, 0.2, 0, pink);

        if (burst) {
            player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, 10, 0, 0, 0, 0.1);
            burst = false;
        }
    }

    private void showSelectedTarget() {
        if (target != null)
            player.getWorld().spawnParticle(
                    Particle.REDSTONE, target.getLocation().add(0, 1, 0),
                    2, 0.5, 1, 0.5, 0, white);
    }

    @Override
    public void remove() {
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
    public double getCollisionRadius() {
        return blastRadius;
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
        return blast != null ? blast : player.getLocation();
    }

    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.LightBlast.Enabled");
    }

    @Override
    public void load() {}
    @Override
    public void stop() {}
}