package me.numin.spirits.ability.spirit;

import java.util.Random;

import me.numin.spirits.utilities.Removal;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.DamageHandler;

import me.numin.spirits.Spirits;
import me.numin.spirits.utilities.Methods;
import me.numin.spirits.utilities.Methods.SpiritType;
import me.numin.spirits.ability.api.SpiritAbility;
import org.bukkit.util.Vector;

public class Possess extends SpiritAbility implements AddonAbility {

    //TODO: Test how it interacts with sudden change in pathway (like the spawning of a RaiseEarth that obstructs it's path)
    //TODO: Test how it interacts with abilities like AirShield and Shelter.
    //TODO: Add configurable speed for the armor stand/blast feature.

    private ArmorStand armorStand;
    private DustOptions purple = new DustOptions(Color.fromRGB(130, 0, 193), 1);
    private Entity target;
    private GameMode originalGameMode;
    private Location blast, playerOrigin;
    private Removal removal;
    private Vector vector = new Vector(1, 0, 0);

    private boolean hasStarted = false, playEssence, wasFlying;
    private double damage, range;
    private float pitch = 0F, volume = 0.1F;
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

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 0.3F, -1);
            Methods.animateVanish(player);

            this.armorStand = this.createArmorStand();
            this.originalGameMode = player.getGameMode();
            this.wasFlying = player.isFlying();

            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(this.armorStand);

            this.removal = new Removal(player, false, target);
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
        if (removal.stop()) {
            remove();
            return;
        }
        if (hasStarted && player.isSneaking()) {
            remove();
            return;
        }
        this.possession();
    }

    private void possession() {
        this.hasStarted = true;
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
        this.blast = Methods.advanceLocationToPoint(vector, this.playerOrigin, targetLocation, 0.9);
        this.armorStand.teleport(this.blast);

        if (new Random().nextInt(5) == 0) {
            player.getWorld().playSound(targetLocation, Sound.ENTITY_EVOKER_CAST_SPELL, this.volume, this.pitch);
            Methods.playSpiritParticles(player, this.blast, 0.5, 0.5, 0.5, 0, 1);
        }

        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, this.blast, 10, 0.2, 0.2, 0.2, 0.02);
        player.getWorld().spawnParticle(Particle.PORTAL, this.blast, 5, 0, 0, 0, 1);
        player.getWorld().spawnParticle(Particle.REDSTONE, this.blast, 5, 0, 0, 0, 1, this.purple);

        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(this.blast, 1.5)) {
            if (entity.equals(target)) {
                this.playEssence = false;
                player.setSpectatorTarget(this.target);
                this.armorStand.remove();
                break;
            }
        }
    }

    private void animateFinalBlow(Location targetLocation) {
        DamageHandler.damageEntity(target, damage, this);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_HURT, 0.2F, 0F);
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
            player.getWorld().playSound(targetLocation, Sound.ENTITY_EVOKER_CAST_SPELL, this.volume, this.pitch);
            if (this.blast != null) Methods.playSpiritParticles(player, this.blast, 0.4, 1, 0.4, 0, 1);
        }
    }

    private ArmorStand createArmorStand() {
        ArmorStand stand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCollidable(false);
        return stand;
    }

    @Override
    public void remove() {
        player.setSpectatorTarget(null);
        player.setGameMode(this.originalGameMode);
        player.setFlying(wasFlying);

        if (this.armorStand != null) this.armorStand.remove();

        if (playEssence) player.teleport(this.blast);
        else player.teleport(player.getLocation().add(0, 2, 0));

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