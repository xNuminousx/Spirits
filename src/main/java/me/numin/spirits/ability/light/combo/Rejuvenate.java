package me.numin.spirits.ability.light.combo;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import me.numin.spirits.Spirits;
import me.numin.spirits.Methods;
import me.numin.spirits.Methods.SpiritType;
import me.numin.spirits.ability.api.LightAbility;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class Rejuvenate extends LightAbility implements AddonAbility, ComboAbility {

    private Location location;
    private Location location2;
    private Location circleCenter;
    private long time;
    private long cooldown;
    private long duration;
    private int radius;
    private int effectInt;
    private boolean damageMonsters;
    private boolean damageDarkSpirits;
    private double damage;
    private double t;
    private int currPoint;
    private Location location3;

    public Rejuvenate(Player player) {
        super(player);

        if (!bPlayer.canBendIgnoreBinds(this)) {
            return;
        }
        setFields();
        time = System.currentTimeMillis();
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.07F, 5);
        start();
        bPlayer.addCooldown(this);
    }

    private void setFields() {
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Cooldown");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Duration");
        this.radius = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Radius");
        this.effectInt = Spirits.plugin.getConfig().getInt("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.EffectInterval");
        this.damage = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Damage");
        this.damageDarkSpirits = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.HurtDarkSpirits");
        this.damageMonsters = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.HurtMonsters");
        location = player.getLocation();
        location2 = player.getLocation();
        location3 = player.getLocation();
        circleCenter = player.getLocation();
    }

    @Override
    public void progress() {
        if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location) || !bPlayer.canBendIgnoreBindsCooldowns(this)) {
            remove();
            return;
        }
        spawnCircle();
        grabEntities();
        if (System.currentTimeMillis() > time + duration) {
            remove();
            return;
        }

    }

    public void spawnCircle() {
        Methods.createPolygon(location, 8, radius, 0.2, ParticleEffect.SPELL_INSTANT);
        for (int i = 0; i < 6; i++) {
            this.currPoint += 360 / 300;
            if (this.currPoint > 360) {
                this.currPoint = 0;
            }
            double angle = this.currPoint * Math.PI / 180.0D;
            double x = radius * Math.cos(angle);
            double x2 = radius * Math.sin(angle);
            double z = radius * Math.sin(angle);
            double z2 = radius * Math.cos(angle);
            location2.add(x, 0, z);
            ParticleEffect.END_ROD.display(location2, 0, 0, 0, 0, 1);
            location2.subtract(x, 0, z);

            location3.add(x2, 0, z2);
            ParticleEffect.END_ROD.display(location3, 0, 0, 0, 0, 1);
            location3.subtract(x2, 0, z2);
        }
        t += Math.PI / 32;
        if (!(t >= Math.PI * 4)) {
            for (double i = 0; i <= Math.PI * 2; i += Math.PI / 1.2) {
                double x = 0.5 * (Math.PI * 4 - t) * Math.cos(t - i);
                double y = 0.4 * t;
                double z = 0.5 * (Math.PI * 4 - t) * Math.sin(t - i);
                location.add(x, y, z);
                Methods.playSpiritParticles(SpiritType.LIGHT, location, 0, 0, 0, 0, 1);
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0.1, 0.1, 0.1, 0, new DustOptions(Color.fromBGR(255, 255, 255), 1));
                location.subtract(x, y, z);
            }
        }

        ParticleEffect.ENCHANTMENT_TABLE.display(location, radius / 2, 0.4F, radius / 2, 0, 10);
    }

    public void grabEntities() {
        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(circleCenter, radius)) {
            if (entity instanceof LivingEntity) {
                healEntities(entity);
            }
        }
    }

    public void healEntities(Entity entity) {
        if (new Random().nextInt(effectInt) == 0) {
            if (entity instanceof Player) {
                Player ePlayer = (Player) entity;
                BendingPlayer bEntity = BendingPlayer.getBendingPlayer(ePlayer);
                if (!bEntity.hasElement(Element.getElement("DarkSpirit"))) {
                    ePlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 0));
                    ParticleEffect.HEART.display(ePlayer.getLocation().add(0, 2, 0), 0, 0, 0, 0, 1);
                } else {
                    if (damageDarkSpirits) {
                        DamageHandler.damageEntity(ePlayer, damage, this);
                    }
                }
            } else if (entity instanceof Monster && damageMonsters) {
                DamageHandler.damageEntity(entity, damage, this);

            } else {
                LivingEntity le = (LivingEntity)entity;
                le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 0));
                ParticleEffect.HEART.display(entity.getLocation().add(0, 2, 0), 0, 0, 0, 0, 1);
            }
        }
    }

    @Override
    public Object createNewComboInstance(Player player) {
        return new Rejuvenate(player);
    }

    @Override
    public ArrayList<AbilityInformation> getCombination() {
        ArrayList<AbilityInformation> combo = new ArrayList<AbilityInformation>();
        combo.add(new AbilityInformation("Alleviate", ClickType.SHIFT_DOWN));
        combo.add(new AbilityInformation("Alleviate", ClickType.RIGHT_CLICK_BLOCK));
        combo.add(new AbilityInformation("Alleviate", ClickType.SHIFT_UP));
        return combo;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public String getName() {
        return "Rejuvenate";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(SpiritType.LIGHT, "Combo") +
                Spirits.plugin.getConfig().getString("Language.Abilities.LightSpirit.Rejuvenate.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.setSpiritDescriptionColor(SpiritType.LIGHT) +
                Spirits.plugin.getConfig().getString("Language.Abilities.LightSpirit.Rejuvenate.Instructions");
    }

    @Override
    public String getAuthor() {
        return Methods.setSpiritDescriptionColor(SpiritType.LIGHT) + Methods.getAuthor();
    }

    @Override
    public String getVersion() {
        return Methods.setSpiritDescriptionColor(SpiritType.LIGHT) + Methods.getVersion();
    }

    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Combo.Rejuvenate.Enabled");
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
    public void load() {
    }

    @Override
    public void stop() {
    }

}