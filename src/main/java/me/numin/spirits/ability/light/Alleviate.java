package me.numin.spirits.ability.light;

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
import me.numin.spirits.ability.api.LightAbility;

public class Alleviate extends LightAbility implements AddonAbility {

    private DustOptions customColor;
    private LivingEntity target;
    private Location location;
    private Removal removal;
    private Vector vector = new Vector(1, 0, 0);

    private boolean hasReached, removeNegPots;
    private double range, selfDamage;
    private int currPoint, healDuration, nightVisDuration;
    private long chargeTime, healInt, otherCooldown, potInt, selfCooldown, time;

    public Alleviate(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        setFields();
        Entity targetEntity = GeneralMethods.getTargetedEntity(player, range);
        if (targetEntity instanceof LivingEntity)
            this.target = (LivingEntity) targetEntity;

        time = System.currentTimeMillis();
        start();
    }

    private void setFields() {
        //Alleviate
        this.otherCooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Others.Cooldown");
        this.range = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.LightSpirit.Alleviate.Others.Range");
        this.potInt = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Others.PotionInterval");
        this.healInt = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Others.HealInterval");
        this.selfDamage = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.LightSpirit.Alleviate.Others.SelfDamage");

        //Sanctity
        this.selfCooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Self.Cooldown");
        this.chargeTime = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Alleviate.Self.ChargeTime");
        this.healDuration = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Alleviate.Self.HealDuration");
        this.nightVisDuration = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Alleviate.Self.NightVisionDuration");
        this.removeNegPots = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Alleviate.Self.RemoveNegativePotionEffects");

        int red = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Alleviate.ParticleColor.Red");
        int green = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Alleviate.ParticleColor.Green");
        int blue = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Alleviate.ParticleColor.Blue");
        this.customColor = new DustOptions(Color.fromRGB(red, green, blue), 1);

        this.location = player.getLocation().clone().add(0, 1, 0);
        this.removal = new Removal(player, true);
    }

    @Override
    public void progress() {
        if (removal.stop() ||
                !bPlayer.getBoundAbilityName().equals(getName())) {
            remove();
            return;
        }

        if (target != null && player.getLocation().distance(target.getLocation()) > range) {
            remove();
            return;
        }

        if (player.isSneaking()) {
            if (target == null) {
                progressSanctity();
            } else {
                if (!hasReached) showSelection();
                else progressAlleviate();
            }
        }
    }

    private void showSelection() {
        Location blast = Methods.advanceLocationToPoint(vector, location, target.getLocation().add(0, 1, 0), 0.5);

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

    private void progressAlleviate() {
        showSpirals(target.getLocation());

        if (System.currentTimeMillis() - time > potInt) {
            for (PotionEffect targetEffect : target.getActivePotionEffects()) {
                if (isNegativeEffect(targetEffect.getType())) {
                    target.removePotionEffect(targetEffect.getType());
                }
            }
            bPlayer.addCooldown(this, otherCooldown);
        }
        if (System.currentTimeMillis() - time > healInt) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, false, true, false));
            DamageHandler.damageEntity(player, selfDamage, this);

            remove();
            return;
        }
        if (new Random().nextInt(20) == 0) {
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
        }
    }

    private void progressSanctity() {
        showSpirals(player.getLocation());

        if (System.currentTimeMillis() > time + chargeTime) {
            for (PotionEffect playerEffects : player.getActivePotionEffects()) {
                if (isNegativeEffect(playerEffects.getType()) && removeNegPots) {
                    player.removePotionEffect(playerEffects.getType());
                }
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, healDuration * 100, 1, false, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, nightVisDuration * 100, 1, false, true, false));

            remove();
            return;
        }

        if (new Random().nextInt(20) == 0)
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
    }

    private void showSpirals(Location location) {
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
        return target != null ? otherCooldown : selfCooldown;
    }

    @Override
    public Location getLocation() {
        return target != null ? target.getLocation() : player.getLocation();
    }

    @Override
    public String getName() {
        return "Alleviate";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(SpiritType.LIGHT, "Utility") +
                Spirits.plugin.getConfig().getString("Language.Abilities.LightSpirit.Alleviate.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(SpiritType.LIGHT) +
                Spirits.plugin.getConfig().getString("Language.Abilities.LightSpirit.Alleviate.Instructions");
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
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Alleviate.Enabled");
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