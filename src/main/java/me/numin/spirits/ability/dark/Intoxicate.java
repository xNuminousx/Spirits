package me.numin.spirits.ability.dark;

import java.util.Random;

import me.numin.spirits.utilities.Removal;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.DamageHandler;

import me.numin.spirits.Spirits;
import me.numin.spirits.utilities.Methods;
import me.numin.spirits.utilities.Methods.SpiritType;
import me.numin.spirits.ability.api.DarkAbility;

public class Intoxicate extends DarkAbility implements AddonAbility {

    //TODO: Make new sounds

    private DustOptions black = new DustOptions(Color.fromRGB(0, 0, 0), 1);
    private DustOptions customColor;
    private LivingEntity target;
    private Location location;
    private Removal removal;
    private Vector vector = new Vector(1, 0, 0);

    private boolean hasReached;
    private double blastSpeed, range, selfDamage;
    private int currPoint;
    private int witherDuration, hungerDuration, confusionDuration;
    private int witherPower, hungerPower, confusionPower;
    private long cooldown, harmInt, potInt, time;

    public Intoxicate(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        setFields();
        Entity targetEntity = GeneralMethods.getTargetedEntity(player, range);
        if (targetEntity instanceof LivingEntity) {
            this.target = (LivingEntity) targetEntity;
            time = System.currentTimeMillis();
            start();
        }
    }

    private void setFields() {
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.Intoxicate.Cooldown");
        this.range = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Intoxicate.Range");
        this.potInt = Spirits.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.Intoxicate.PotionInterval");
        this.harmInt = Spirits.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.Intoxicate.HarmInterval");
        this.selfDamage = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Intoxicate.SelfDamage");
        this.blastSpeed = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Intoxicate.BlastSpeed");
        this.witherDuration = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.Potions.WitherDuration");
        this.witherPower = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.Potions.WitherPower");
        this.hungerDuration = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.Potions.HungerDuration");
        this.hungerPower = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.Potions.HungerPower");
        this.confusionDuration = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.Potions.ConfusionDuration");
        this.confusionPower = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.Potions.ConfusionPower");

        int red = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.ParticleColor.Red");
        int green = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.ParticleColor.Green");
        int blue = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Intoxicate.ParticleColor.Blue");
        this.customColor = new DustOptions(Color.fromRGB(red, green, blue), 1);

        this.location = player.getLocation().clone().add(0, 1, 0);
        this.removal = new Removal(player, true);
    }

    @Override
    public void progress() {
        if (removal.stop() ||
                !bPlayer.getBoundAbilityName().equals(getName()) ||
                player.getLocation().distance(target.getLocation()) > range) {
            remove();
            return;
        }

        if (player.isSneaking() && target != null) {
            if (!hasReached) showSelection();
            else doEffects();
        }
    }

    private void showSelection() {
        Location blast = Methods.advanceLocationToPoint(vector, location, target.getLocation().add(0, 1, 0), blastSpeed);

        player.getWorld().spawnParticle(Particle.REDSTONE, blast, 1, 0.1, 0.1, 0.1, 0, black);
        player.getWorld().spawnParticle(Particle.REDSTONE, blast, 1, 0.1, 0.1, 0.1, 0, customColor);

        if (blast.distance(player.getLocation()) > range ||
                blast.getBlock().isLiquid() ||
                GeneralMethods.isSolid(blast.getBlock())) {
            remove();
            return;
        }

        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(blast, 1.4)) {
            if (entity.equals(target)) {
                hasReached = true;
                break;
            }
        }
    }

    private void doEffects() {
        showSpirals();

        if (System.currentTimeMillis() - time > potInt) {
            for (PotionEffect targetEffect : target.getActivePotionEffects()) {
                if (isPositiveEffect(targetEffect.getType())) {
                    target.removePotionEffect(targetEffect.getType());
                }
            }
        }

        if (System.currentTimeMillis() - time > harmInt) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * witherDuration, witherPower, false, true, false));
            target.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20 * hungerDuration, hungerPower, false, true, false));
            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * confusionDuration, confusionPower, false, true, false));
            DamageHandler.damageEntity(player, selfDamage, this);
            remove();
            return;
        }

        if (new Random().nextInt(20) == 0) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, -1);
        }
    }

    private void showSpirals() {
        if (target == null) return;

        Location location = target.getLocation();
        for (int i = 0; i < 6; i++) {
            currPoint += 360 / 200;
            if (currPoint > 360) {
                currPoint = 0;
            }
            double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
            double x = (float) 0.04 * (Math.PI * 4 - angle) * Math.cos(angle + i);
            double y = 1.2 * Math.cos(angle) + 1.2;
            double z = (float) 0.04 * (Math.PI * 4 - angle) * Math.sin(angle + i);
            location.add(x, y, z);
            player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, black);
            player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, customColor);
            location.subtract(x, y, z);
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
        return target != null ? target.getLocation() : player.getLocation();
    }

    @Override
    public String getName() {
        return "Intoxicate";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(SpiritType.DARK, "Offense") +
                Spirits.plugin.getConfig().getString("Language.Abilities.DarkSpirit.Intoxicate.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(SpiritType.DARK) +
                Spirits.plugin.getConfig().getString("Language.Abilities.DarkSpirit.Intoxicate.Instructions");
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
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Intoxicate.Enabled");
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
        return true;
    }

    @Override
    public void load() {}
    @Override
    public void stop() {}
}