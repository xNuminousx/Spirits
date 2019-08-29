package me.numin.spirits.ability.spirit;

import me.numin.spirits.ability.api.Removal;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.AddonAbility;

import me.numin.spirits.Spirits;
import me.numin.spirits.Methods;
import me.numin.spirits.Methods.SpiritType;
import me.numin.spirits.ability.api.SpiritAbility;

public class Dash extends SpiritAbility implements AddonAbility {

    private long dCooldown, distance;
    private Location location;
    private Removal removal;

    public Dash(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        setFields();
        start();
    }

    private void setFields() {
        this.dCooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Dash.Cooldown");
        this.distance = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Dash.Distance");
        this.location = player.getLocation();
        this.removal = new Removal(player);
    }

    @Override
    public void progress() {
        if (removal.stop()) {
            remove();
            return;
        }
        progressDash();
    }

    private void progressDash() {
        player.setVelocity(Methods.setVelocity(player, distance, 0.2));
        location.getWorld().playSound(location, Sound.ENTITY_ELDER_GUARDIAN_HURT, 1.5F, 0.5F);
        location.getWorld().playSound(location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.3F, 0.5F);
        Methods.playSpiritParticles(player, player.getLocation(), 0.5, 0.5, 0.5, 0, 10);
        remove();
    }

    @Override
    public void remove() {
        bPlayer.addCooldown("Dash", dCooldown);
        super.remove();
    }

    @Override
    public long getCooldown() {
        return dCooldown;
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
        return false;
    }

    @Override
    public void load() {}
    @Override
    public void stop() {}
}