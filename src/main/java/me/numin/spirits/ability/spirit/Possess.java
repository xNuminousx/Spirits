package me.numin.spirits.ability.spirit;

import java.util.Random;

import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.DamageHandler;

import me.numin.spirits.Spirits;
import me.numin.spirits.Methods;
import me.numin.spirits.Methods.SpiritType;
import me.numin.spirits.ability.api.SpiritAbility;
import org.bukkit.util.Vector;

public class Possess extends SpiritAbility implements AddonAbility {

    private DustOptions purple = new DustOptions(Color.fromRGB(130, 0, 193), 1);
    private Entity target;
    private GameMode originalGameMode;
    private Location blast, playerOrigin;
    private Vector vector = new Vector(1, 0, 0);

    private boolean playEssence;
    private double damage, range;
    private long cooldown, duration, time;

    public Possess(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        setFields();
        this.target = GeneralMethods.getTargetedEntity(player, range);
        if (target != null) {
            this.time = System.currentTimeMillis();

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.2F, 1);
            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation().add(0, 1, 0), 40, 0, 0, 0, 1);

            this.originalGameMode = player.getGameMode();
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(target);

            start();
        }
    }

    private void setFields() {
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Possess.Cooldown");
        this.range = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Possess.Range");
        this.damage = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Possess.Damage");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Possess.Duration");
        this.playerOrigin = player.getLocation().add(0, 1, 0);
        this.playEssence = true;
    }

    @Override
    public void progress() {
        if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, player.getLocation())) {
            remove();
            return;
        }

        if (target.isDead() || target.getWorld() != player.getWorld() ||GeneralMethods.isRegionProtectedFromBuild(this, target.getLocation())) {
            remove();
            return;
        }

        if (target instanceof Player && !((Player)target).isOnline()) {
            remove();
            return;
        }

        if (bPlayer.isParalyzed() || !bPlayer.getBoundAbilityName().equals(getName())) {
            remove();
            return;
        }

        if (player.isSneaking()) {
            remove();
            return;
        }

        this.possession();
    }

    private void possession() {
        Location targetLocation = target.getLocation().add(0, 1, 0);

        if (System.currentTimeMillis() > time + duration) {
            this.animateFinalBlow(targetLocation);
        } else {
            this.animateTargetEffects(targetLocation);
            if (this.playEssence) {
                this.animateEssence(targetLocation);
            } else {
                target.getWorld().spawnParticle(Particle.DRAGON_BREATH, targetLocation, 1, 0.3, 1, 0.3, 0.02);
            }
        }
    }

    private void animateEssence(Location targetLocation) {
        this.blast = Methods.advanceLocationToPoint(vector, this.playerOrigin, targetLocation);

        if (new Random().nextInt(5) == 0) {
            player.getWorld().playSound(targetLocation, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.1F, 2);
            Methods.playSpiritParticles(player, this.blast, 0.5F, 0.5F, 0.5F, 0, 1);
        }

        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, this.blast, 1, 0.2, 0.2, 0.2, 0.02);
        player.getWorld().spawnParticle(Particle.PORTAL, this.blast, 1, 0, 0, 0, 1);
        player.getWorld().spawnParticle(Particle.REDSTONE, this.blast, 1, 0, 0, 0, 1, this.purple);

        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(this.blast, 1.5)) {
            if (entity.equals(target)) {
                this.playEssence = false;
                break;
            }
        }
    }

    private void animateFinalBlow(Location targetLocation) {
        DamageHandler.damageEntity(target, damage, this);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.5F);
        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, targetLocation, 1, 0, 0, 0, 0);
        player.getWorld().spawnParticle(Particle.CRIT, targetLocation, 5, 0.3, 1, 0.3, 0);
        remove();
    }

    private void animateTargetEffects(Location targetLocation) {
        if (target instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity)target;
            livingTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 2), true);
            livingTarget.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2), true);
        }

        if (new Random().nextInt(5) == 0) {
            player.getWorld().playSound(targetLocation, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.1F, 2);
            Methods.playSpiritParticles(player, this.blast, 0.4F, 1F, 0.4F, 0, 1);
        }
    }

    @Override
    public void remove() {
        player.setSpectatorTarget(null);
        player.setGameMode(this.originalGameMode);
        player.teleport(player.getLocation().add(0, 2, 0));

        bPlayer.addCooldown(this);
        super.remove();
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public Location getLocation() {
        return target != null ? target.getLocation() : player.getLocation();
    }

    @Override
    public String getName() {
        return "Possess";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(SpiritType.NEUTRAL, "Offense") +
                Spirits.plugin.getConfig().getString("Language.Abilities.Spirit.Possess.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(SpiritType.NEUTRAL) +
                Spirits.plugin.getConfig().getString("Language.Abilities.Spirit.Possess.Instructions");
    }

    @Override
    public String getAuthor() {
        return Methods.getSpiritColor(SpiritType.NEUTRAL) + "" + Methods.getAuthor();
    }

    @Override
    public String getVersion() {
        return Methods.getSpiritColor(SpiritType.NEUTRAL) + Methods.getVersion();
    }

    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Possess.Enabled");
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