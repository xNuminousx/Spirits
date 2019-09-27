package me.numin.spirits.ability.dark;

import me.numin.spirits.ability.api.removal.Removal;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.DamageHandler;

import me.numin.spirits.Spirits;
import me.numin.spirits.Methods;
import me.numin.spirits.Methods.SpiritType;
import me.numin.spirits.ability.api.DarkAbility;

public class Strike extends DarkAbility implements AddonAbility {

    private Entity target;
    private Location location;
    private Location origin;
    private Removal removal;
    private Vector direction;

    private boolean checkEntities;
    private double damage, radius;
    private int range;
    private long cooldown;

    public Strike(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        setFields();
        start();
    }

    private void setFields() {
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.Strike.Cooldown");
        this.damage = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Strike.Damage");
        this.range = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Strike.Range");
        this.radius = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Strike.Radius");
        this.origin = player.getLocation().clone().add(0, 1, 0);
        this.location = origin.clone();
        this.direction = player.getLocation().getDirection();
        this.checkEntities = true;
        this.removal = new Removal(player);
    }

    @Override
    public void progress() {
        if (removal.stop()) {
            remove();
            return;
        }
        strike();

    }

    private void strike() {
        if (checkEntities) {
            Location blast = location.add(direction.multiply(1).normalize());
            blast.getWorld().spawnParticle(Particle.CRIT, blast, 1, 0, 0, 0, 0);

            if (origin.distance(blast) > range) {
                remove();
                return;
            }
            for (Entity entity : GeneralMethods.getEntitiesAroundPoint(blast, radius)) {
                if (entity instanceof LivingEntity && !entity.getUniqueId().equals(player.getUniqueId())) {
                    this.target = entity;
                    checkEntities = false;
                }
            }
        } else {
            if (this.target == null) return;
            player.teleport(this.target.getLocation());
            DamageHandler.damageEntity(target, damage, this);
            player.getWorld().playSound(location, Sound.ENTITY_PLAYER_BURP, 0.2F, 0.2F);
            remove();
        }
    }

    @Override
    public void remove() {
        bPlayer.addCooldown(this);
        super.remove();
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public double getCollisionRadius() {
        return radius;
    }

    @Override
    public String getName() {
        return "Strike";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(SpiritType.DARK, "Offense") +
                Spirits.plugin.getConfig().getString("Language.Abilities.DarkSpirit.Strike.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(SpiritType.DARK) + Spirits.plugin.getConfig().getString("Language.Abilities.DarkSpirit.Strike.Instructions");
    }

    @Override
    public String getAuthor() {
        return Methods.getSpiritColor(SpiritType.DARK) + "" + Methods.getAuthor();
    }

    @Override
    public String getVersion() {
        return Methods.getSpiritColor(SpiritType.DARK) + Methods.getVersion();
    }

    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Strike.Enabled");
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