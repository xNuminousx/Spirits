package me.numin.spirits.ability.spirit;

import java.util.Random;

import me.numin.spirits.utilities.Removal;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;

import me.numin.spirits.Spirits;
import me.numin.spirits.utilities.Methods;
import me.numin.spirits.utilities.Methods.SpiritType;
import me.numin.spirits.ability.api.SpiritAbility;

public class Soar extends SpiritAbility implements AddonAbility {

    //TODO: Update sounds.

    private Removal removal;

    private double speed;
    private long cooldown, duration, time;

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
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Soar.Cooldown");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Soar.Duration");
        this.speed = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Agility.Soar.Speed");
        this.removal = new Removal(player, true);
    }

    @Override
    public void progress() {
        if (removal.stop()) {
            remove();
            return;
        }
        progressSoar();
    }

    private void progressSoar() {
        if (player.isSneaking()) {
            if (System.currentTimeMillis() > time + duration) {
                remove();
            } else {
                player.setVelocity(Methods.setVelocity(player, (float)speed));
                if (new Random().nextInt(5) == 0) {
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.3F, 5F);
                }
                Methods.playSpiritParticles(player, player.getLocation(), 0.5, 0.5, 0.5, 0, 2);
            }
        }
    }

    @Override
    public void remove() {
        bPlayer.addCooldown("Soar", cooldown);
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
    public void load() {}
    @Override
    public void stop() {}
}