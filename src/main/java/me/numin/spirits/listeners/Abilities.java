package me.numin.spirits.listeners;

import com.projectkorra.projectkorra.ability.CoreAbility;
import me.numin.spirits.ability.dark.DarkBlast;
import me.numin.spirits.ability.light.LightBlast;
import me.numin.spirits.ability.spirit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

public class Abilities implements Listener {

    @EventHandler
    public void onClick(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (event.isCancelled() || bPlayer == null) return;

        if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Possess")) {
            new Possess(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Agility")) {
            if (bPlayer.isOnCooldown("Dash")) return;
            new Dash(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Shackle")) {
            new Shackle(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Shelter")) {
            new Shelter(player, ShelterType.CLICK);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Strike")) {
            new Strike(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("LightBlast")) {
            if (CoreAbility.hasAbility(player, LightBlast.class)) {
                LightBlast lightBlast = CoreAbility.getAbility(player, LightBlast.class);
                if (lightBlast.getAction() == LightBlast.Action.SHIFT) {
                    lightBlast.canHeal = true;
                }
            } else {
                new LightBlast(player, LightBlast.Action.CLICK);
            }
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("DarkBlast")) {
            if (CoreAbility.hasAbility(player, DarkBlast.class)) {
                DarkBlast darkBlast = CoreAbility.getAbility(player, DarkBlast.class);
                if (darkBlast.getAction() == DarkBlast.Type.SHIFT && !darkBlast.hasReached) {
                    darkBlast.canShoot = true;
                }
            } else {
                new DarkBlast(player, DarkBlast.Type.CLICK);
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (event.isCancelled() || bPlayer == null) return;

        if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Alleviate")) {
            new Alleviate(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Intoxicate")) {
            new Intoxicate(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Agility") && event.isSneaking()) {
            if (bPlayer.isOnCooldown("Soar")) return;
            new Soar(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Shelter")) {
            new Shelter(player, ShelterType.SHIFT);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Vanish")) {
            new Vanish(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Orb")) {
            new Orb(player);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("LightBlast")) {
            new LightBlast(player, LightBlast.Action.SHIFT);
        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("DarkBlast") && !CoreAbility.hasAbility(player, DarkBlast.class)) {
            new DarkBlast(player, DarkBlast.Type.SHIFT);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (event.isCancelled() || bPlayer == null) {
            return;
        }
        if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Vanish")) {
            if (event.getCause() == TeleportCause.SPECTATE) {
                event.setCancelled(true);
            }
        }
    }
}