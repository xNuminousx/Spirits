package me.numin.spirits.ability.spirit.combo;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.util.ClickType;
import me.numin.spirits.utilities.Methods;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.SpiritAbility;
import me.numin.spirits.utilities.Removal;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class Levitation extends SpiritAbility implements AddonAbility, ComboAbility {

    //TODO: Implement sounds.

    private Location origin;
    private Removal removal;

    private boolean doAgilityMultiplier, doLevitationMultiplier, doPhaseMultiplier, wasFlying;
    private double allowedHealthLoss, initialHealth, agilityMultiplier, levitationMultiplier, phaseMultiplier, range;
    private long cooldown, duration, time;

    public Levitation(Player player) {
        super(player);

        if (!bPlayer.canBendIgnoreBinds(this) || CoreAbility.hasAbility(player, Levitation.class)) return;

        player.teleport(player.getLocation().add(0, 0.3, 0));
        setFields();
        start();
    }

    private void setFields() {
        //Config
        this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Levitation.Cooldown");
        this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Combo.Levitation.Duration");
        this.range = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Combo.Levitation.Range");
        this.allowedHealthLoss = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Combo.Levitation.AllowedHealthLoss");
        this.doAgilityMultiplier = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Levitation.AbilityCooldownMultipliers.Agility.Enabled");
        this.agilityMultiplier = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Combo.Levitation.AbilityCooldownMultipliers.Agility.Multiplier");
        this.doPhaseMultiplier = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Levitation.AbilityCooldownMultipliers.Phase.Enabled");
        this.phaseMultiplier = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Combo.Levitation.AbilityCooldownMultipliers.Phase.Multiplier");
        this.doLevitationMultiplier = Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Levitation.AbilityCooldownMultipliers.Levitation.Enabled");
        this.levitationMultiplier = Spirits.plugin.getConfig().getDouble("Abilities.Spirits.Neutral.Combo.Levitation.AbilityCooldownMultipliers.Levitation.Multiplier");

        this.wasFlying = player.isFlying();
        this.origin = player.getLocation();
        this.removal = new Removal(player);
        this.time = System.currentTimeMillis();
        this.initialHealth = player.getHealth();
    }

    @Override
    public void progress() {
        if (removal.stop() ||
                origin.distance(player.getLocation()) > range ||
                (player.getHealth() < (initialHealth - allowedHealthLoss)) ||
                System.currentTimeMillis() > time + duration) {
            remove();
            return;
        }
        player.setFlying(true);
        this.playParticles();
    }

    private void playParticles() {
        Location location = player.getLocation().add(0, 1, 0);
        location.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 1, 0.3, 0.3, 0.3, 0.01);
        Methods.playSpiritParticles(player, location, 0.4, 0.6, 0.4, 0, 1);
    }

    @Override
    public void remove() {
        player.setFlying(wasFlying);

        long duration = System.currentTimeMillis() - time;
        if (doAgilityMultiplier) {
            long agilityCooldown = (long) (duration * agilityMultiplier);
            bPlayer.addCooldown("Dash", agilityCooldown);
            bPlayer.addCooldown("Soar", agilityCooldown);
        }
        if (doPhaseMultiplier) {
            long phaseCooldown = (long) (duration * phaseMultiplier);
            bPlayer.addCooldown("Phase", phaseCooldown);
        }
        if (doLevitationMultiplier) {
            long levitationCooldown = (long) (duration * levitationMultiplier) + cooldown;
            bPlayer.addCooldown("Levitation", levitationCooldown);
        } else {
            bPlayer.addCooldown(this, cooldown);
        }
        super.remove();
    }

    @Override
    public Object createNewComboInstance(Player player) {
        return new Levitation(player);
    }

    @Override
    public ArrayList<ComboManager.AbilityInformation> getCombination() {
        ArrayList<AbilityInformation> combo = new ArrayList<AbilityInformation>();
        combo.add(new ComboManager.AbilityInformation("Agility", ClickType.LEFT_CLICK));
        combo.add(new ComboManager.AbilityInformation("Agility", ClickType.SHIFT_DOWN));
        combo.add(new ComboManager.AbilityInformation("Vanish", ClickType.LEFT_CLICK));
        return combo;
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
        return "Levitation";
    }

    @Override
    public String getDescription() {
        return Methods.setSpiritDescription(Methods.SpiritType.NEUTRAL, "Combo") +
                Spirits.plugin.getConfig().getString("Language.Abilities.Spirit.Levitation.Description");
    }

    @Override
    public String getInstructions() {
        return Methods.getSpiritColor(Methods.SpiritType.NEUTRAL) +
                Spirits.plugin.getConfig().getString("Language.Abilities.Spirit.Levitation.Instructions");
    }

    @Override
    public String getAuthor() {
        return Methods.getSpiritColor(Methods.SpiritType.NEUTRAL) + "" + Methods.getAuthor();
    }

    @Override
    public String getVersion() {
        return Methods.getSpiritColor(Methods.SpiritType.NEUTRAL) + Methods.getVersion();
    }

    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.Neutral.Combo.Levitation.Enabled");
    }

    @Override
    public boolean isSneakAbility() {
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
    public boolean isExplosiveAbility() {
        return false;
    }

    @Override
    public void load() {}
    @Override
    public void stop() {}
}