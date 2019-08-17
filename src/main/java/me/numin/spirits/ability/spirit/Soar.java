package me.numin.spirits.ability.spirit;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;

import me.numin.spirits.Spirits;
import me.numin.spirits.Methods;
import me.numin.spirits.Methods.SpiritType;
import me.numin.spirits.ability.api.SpiritAbility;

public class Soar extends SpiritAbility implements AddonAbility {

    private long time;
    private long duration;
    private double speed;
    private Location location;
    private long soarCooldown;

    public Soar(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }
        time = System.currentTimeMillis();
        setFields();
        start();
    }

    private void setFields() {
        this.soarCooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Soar.Cooldown");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Soar.Duration");
        this.speed = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Agility.Soar.Speed");
        this.location = player.getLocation();
    }

    @Override
    public void progress() {
        if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
            remove();
            return;
        }
        progressSoar();
    }

    private void progressSoar() {
        if (player.isSneaking()) {
            if (System.currentTimeMillis() > time + duration) {
                bPlayer.addCooldown("Soar", soarCooldown);
                remove();
            } else {
                player.setVelocity(Methods.setVelocity(player, (float)speed));
                if (new Random().nextInt(5) == 0) {
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.3F, 5F);
                }
                Methods.playSpiritParticles(player, player.getLocation(), 0.5F, 0.5f, 0.5F, 0, 2);
            }
        } else {
            bPlayer.addCooldown("Soar", soarCooldown);
            remove();
        }
    }

    @Override
    public long getCooldown() {
        return soarCooldown;
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public String getName() {
        return "Agility";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(SpiritType.NEUTRAL, "Mobility") +
                Spirits.plugin.getConfig().getString("Language.Abilities.Spirit.Agility.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(SpiritType.NEUTRAL) + Spirits.plugin.getConfig().getString("Language.Abilities.Spirit.Agility.Instructions");
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
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Agility.Enabled");
    }

    @Override
    public boolean isExplosiveAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return true;
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
    public void load() {
    }

    @Override
    public void stop() {
    }
}