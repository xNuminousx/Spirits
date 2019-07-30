package me.numin.spirits.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;

import me.numin.spirits.ability.dark.Intoxicate;
import me.numin.spirits.ability.dark.Shackle;
import me.numin.spirits.ability.dark.Strike;
import me.numin.spirits.ability.light.Alleviate;
import me.numin.spirits.ability.light.Orb;
import me.numin.spirits.ability.light.Shelter;
import me.numin.spirits.ability.light.Shelter.ShelterType;
import me.numin.spirits.ability.spirit.Dash;
import me.numin.spirits.ability.spirit.Possess;
import me.numin.spirits.ability.spirit.Soar;
import me.numin.spirits.ability.spirit.Vanish;

public class Abilities implements Listener {

    private boolean isPossessing;

    @EventHandler
    public void onSwing(PlayerAnimationEvent event) {

        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (event.isCancelled() || bPlayer == null) {
            return;

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
            return;

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Agility")) {
            new Dash(player);

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Shackle")) {
            new Shackle(player);

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Shelter")) {
            new Shelter(player, ShelterType.CLICK);

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Strike")) {
            new Strike(player);

        }

    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {

        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (event.isCancelled() || bPlayer == null) {
            return;

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
            return;

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Possess")) {
            if (event.isSneaking()) {
                new Possess(player);
                isPossessing = true;
            } else {
                isPossessing = false;
                return;
            }

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Alleviate")) {
            new Alleviate(player);

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Intoxicate")) {
            new Intoxicate(player);

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Agility")) {
            new Soar(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Shelter")) {
            new Shelter(player, ShelterType.SHIFT);

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Vanish")) {
            new Vanish(player);

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Orb")) {
            new Orb(player);

        }

    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (event.isCancelled() || bPlayer == null) {
            return;
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
            return;
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Vanish")) {
            if (event.getCause() == TeleportCause.SPECTATE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
            String boundAbility = bPlayer.getBoundAbilityName();

            if (boundAbility.equalsIgnoreCase("Possess") && isPossessing && event.getCause() == DamageCause.CONTACT) {
                event.setCancelled(true);
                return;
            }
        }
    }
}