package me.numin.spirits.ability.spirit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;

import me.numin.spirits.Spirits;
import me.numin.spirits.Methods;
import me.numin.spirits.Methods.SpiritType;
import me.numin.spirits.ability.api.SpiritAbility;

public class Dash extends SpiritAbility implements AddonAbility {

    private long dashCooldown;
    private long distance;
    private Location location;

    public Dash(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        setFields();
        start();
    }

    private void setFields() {
        this.dashCooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Dash.Cooldown");
        this.distance = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Agility.Dash.Distance");
        this.location = player.getLocation();
    }

    @Override
    public void progress() {
        if (player.isDead() || !player.isOnline() || GeneralMethods.isRegionProtectedFromBuild(this, location)) {
            remove();
            return;
        }
        if (bPlayer.isOnCooldown("Dash")) {
            remove();
            return;
        } else {
            progressDash();
        }
    }

    private void progressDash() {
        Location loc = player.getLocation();
        player.setVelocity(Methods.setVelocity(player, distance, 0.2));
        loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_HURT, 1.5F, 0.5F);
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.3F, 0.5F);
        Methods.playSpiritParticles(player, player.getLocation(), 0.5, 0.5, 0.5, 0, 10);
        bPlayer.addCooldown("Dash", dashCooldown);
        remove();
        return;
    }

    @Override
    public long getCooldown() {
        return dashCooldown;
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