package me.numin.spirits.ability.dark;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import me.numin.spirits.ability.api.DarkAbility;
import me.numin.spirits.utilities.Methods;
import me.numin.spirits.Spirits;
import me.numin.spirits.utilities.Removal;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class DarkBlast extends DarkAbility implements AddonAbility {

    //TODO: Add sounds.

    private DustOptions black = new DustOptions(Color.fromRGB(0, 0, 0), 1);
    private DustOptions purple = new DustOptions(Color.fromRGB(150, 0, 216), 1);
    private Entity target;
    private DarkBlastType type;
    private Location blast, location, origin;
    private Removal removal;
    private Vector direction, vector;

    private boolean burst = true, canDamage, controllable, hasReached = false;
    private double blastRadius, damage, finalBlastSpeed, initialBlastSpeed, range;
    private int potionDuration, potionPower;
    private long cooldown, selectionDuration, time;

    public enum DarkBlastType {
        SHIFT, CLICK
    }

    public DarkBlast(Player player, DarkBlastType type) {
        super(player);

        if (!bPlayer.canBend(this)) return;

        if (type != null) this.type = type;

        if (hasAbility(player, DarkBlast.class)) {
            DarkBlast darkBlast = getAbility(player, DarkBlast.class);
            if (darkBlast.target != null) {
                // Makes sure the player is looking at their target.
                Entity targetEntity = GeneralMethods.getTargetedEntity(player, darkBlast.range);
                if (targetEntity == null || !targetEntity.equals(darkBlast.target)) return;

                darkBlast.location = player.getLocation().add(0, 1, 0);
                darkBlast.canDamage = true;
            }
        } else {
            setFields();
            start();
            time = System.currentTimeMillis();
        }
    }

    private void setFields() {
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.DarkBlast.Cooldown");
        this.controllable = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.DarkBlast.Controllable");
        this.damage = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.DarkBlast.Damage");
        this.range = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.DarkBlast.Range");
        this.selectionDuration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.DarkBlast.DurationOfSelection");
        this.potionDuration = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.DarkBlast.PotionDuration");
        this.potionPower = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.DarkBlast.PotionPower");
        this.initialBlastSpeed = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.DarkBlast.FirstBlastSpeed");
        this.blastRadius = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.DarkBlast.BlastRadius");
        this.finalBlastSpeed = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.DarkBlast.SecondBlastSpeed");

        this.direction = player.getLocation().getDirection();
        this.origin = player.getLocation().add(0, 1, 0);
        this.location = origin.clone();

        this.removal = new Removal(player);
        this.vector = new Vector(1, 0, 0);

        this.canDamage = false;
    }

    @Override
    public void progress() {
        if (this.removal.stop()) {
            remove();
            return;
        }

        if (type == DarkBlastType.CLICK) shootDamagingBlast();
        else if (type == DarkBlastType.SHIFT) shootSelectionBlast();

        showSelectedTarget();

        if (canDamage) shootHomingBlast();
    }

    private void shootDamagingBlast() {
        if (controllable) this.direction = player.getLocation().getDirection();
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
                player.getWorld().spawnParticle(Particle.DRAGON_BREATH, target.getLocation().add(0, 1, 0), 10, 0, 0, 0, 0.2);
                remove();
            }
        }
    }

    private void shootSelectionBlast() {
        if (target == null) {
            if (controllable) this.direction = player.getLocation().getDirection();
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
                    (System.currentTimeMillis() > time + selectionDuration && !canDamage)) {
                remove();
            }
        }
    }

    private void shootHomingBlast() {
        if (!hasReached) {
            this.blast = Methods.advanceLocationToPoint(vector, location, target.getLocation().add(0, 1,0), this.finalBlastSpeed);

            player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.1, 0.1, 0.1, 0, purple);

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
            this.infectEntity(target);
        }
    }

    private void infectEntity(Entity entity) {
        if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
            LivingEntity livingEntity = (LivingEntity)entity;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                    20 * this.potionDuration, this.potionPower, false, true, false));
            remove();
        }
    }

    private void genericBlast(Location location, boolean healing) {
        player.getWorld().spawnParticle(Particle.TOWN_AURA, location, 10, 0.1, 0.1, 0.1, 1);
        player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.2, 0.2, 0.2, 0, black);
        if (healing) player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.2, 0.2, 0.2, 0, purple);
        if (burst) {
            player.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 10, 0, 0, 0, 0.1);
            burst = false;
        }
    }

    private void showSelectedTarget() {
        if (target != null)
            player.getWorld().spawnParticle(
                    Particle.TOWN_AURA, target.getLocation().add(0, 1, 0),
                    10, 0.5, 1, 0.5, 0);
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
        return "DarkBlast";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(Methods.SpiritType.DARK, "Offense") +
                Spirits.plugin.getConfig().getString("Language.Abilities.DarkSpirit.DarkBlast.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(Methods.SpiritType.DARK) +
                Spirits.plugin.getConfig().getString("Language.Abilities.DarkSpirit.DarkBlast.Instructions");
    }

    @Override
    public String getAuthor() {
        return Methods.getSpiritColor(Methods.SpiritType.DARK) + "" + Methods.getAuthor();
    }

    @Override
    public String getVersion() {
        return Methods.getSpiritColor(Methods.SpiritType.DARK) + Methods.getVersion();
    }

    @Override
    public Location getLocation() {
        return blast != null ? blast : player.getLocation();
    }

    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.DarkBlast.Enabled");
    }

    @Override
    public void load() {}
    @Override
    public void stop() {}
}