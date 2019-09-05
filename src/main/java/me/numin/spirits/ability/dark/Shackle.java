package me.numin.spirits.ability.dark;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import me.numin.spirits.Spirits;
import me.numin.spirits.Methods;
import me.numin.spirits.Methods.SpiritType;
import me.numin.spirits.ability.api.DarkAbility;
import me.numin.spirits.ability.api.removal.Removal;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Shackle extends DarkAbility implements AddonAbility {

    private LivingEntity target;
    private Location location, origin, targetLoc;
    private Removal removal;
    private Vector direction;

    private boolean progress;
    private double radius;
    private int currPoint, range;
    private long cooldown, duration, time;

    public Shackle(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        setFields();
        time = System.currentTimeMillis();
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, -1);
        start();
    }

    private void setFields() {
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.Shackle.Cooldown");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.DarkSpirit.Shackle.Duration");
        this.range = Spirits.plugin.getConfig().getInt("Abilities.Spirits.DarkSpirit.Shackle.Range");
        this.radius = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.DarkSpirit.Shackle.Radius");
        this.origin = player.getLocation().clone().add(0, 1, 0);
        this.location = origin.clone();
        this.direction = player.getLocation().getDirection();
        this.progress = true;
        this.removal = new Removal(player);
    }

    @Override
    public void progress() {
        if (removal.stop()) {
            remove();
            return;
        }
        if ((origin.distanceSquared(location) > range * range) && target == null) {
            bPlayer.addCooldown(this, 1000);
            remove();
            return;

        }
        bind();
    }

    private void bind() {
        bPlayer.addCooldown(this);
        if (progress) {
            location.add(direction.multiply(1));
            blastSpiral(location);
        }
        if (target == null) {
            for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, radius)) {
                if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
                    target = (LivingEntity) entity;
                    targetLoc = entity.getLocation();
                }
            }
        } else {
            if (target.isDead() || target.getWorld() != player.getWorld()) {
                remove();
                return;
            }
            if (System.currentTimeMillis() > time + duration) {
                ParticleEffect.CLOUD.display(targetLoc, 0, 0, 0, (int) 0.08F, 5);
                player.getWorld().playSound(targetLoc, Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 0.5F, 1.5F);
                bPlayer.addCooldown(this);
                remove();
                return;
            } else {
                for (Entity entity : GeneralMethods.getEntitiesAroundPoint(targetLoc, 2)) {
                    if (entity != target) {
                        target.teleport(targetLoc);
                    }
                }
                this.progress = false;
                Vector vec = targetLoc.getDirection().normalize().multiply(0);
                target.setVelocity(vec);
                targetLoc.setPitch(targetLoc.getPitch());
                targetLoc.setYaw(targetLoc.getYaw());

                holdSpiral(target.getLocation());
            }
        }
    }

    private void blastSpiral(Location location) {
        for (int i = 0; i < 6; i++) {
            currPoint += 360 / 200;
            if (currPoint > 360) {
                currPoint = 0;
            }
            double angle = currPoint * Math.PI / 180 * Math.cos(Math.PI);
            double x = (float) 0.04 * (Math.PI * 4 - angle) * Math.cos(angle + i);
            double z = (float) 0.04 * (Math.PI * 4 - angle) * Math.sin(angle + i);
            location.add(x, 0.1F, z);
            ParticleEffect.SPELL_WITCH.display(location, 0, 0, 0, 0, 1);
            location.subtract(x, 0.1F, z);
        }
    }

    private void holdSpiral(Location location) {
        for (int t = 0; t < 2; t++) {
            currPoint += 360 / 30;
            if (currPoint > 360) {
                currPoint = 0;
            }
            double angle2 = currPoint * Math.PI / 180 * Math.cos(Math.PI);
            double x2 = (float) 0.04 * (Math.PI * 5 - angle2) * Math.cos(angle2 + t);
            double z2 = (float) 0.04 * (Math.PI * 5 - angle2) * Math.sin(angle2 + t);
            location.add(x2, 0.1F, z2);
            ParticleEffect.SPELL_WITCH.display(location, 0, 0, 0, 0, 1);
            location.subtract(x2, 0.1F, z2);
        }
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public double getCollisionRadius() {
        return radius;
    }

    @Override
    public String getName() {
        return "Shackle";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(SpiritType.DARK, "Defense") +
                Spirits.plugin.getConfig().getString("Language.Abilities.DarkSpirit.Shackle.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(SpiritType.DARK) + Spirits.plugin.getConfig().getString("Language.Abilities.DarkSpirit.Shackle.Instructions");
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
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.DarkSpirit.Shackle.Enabled");
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