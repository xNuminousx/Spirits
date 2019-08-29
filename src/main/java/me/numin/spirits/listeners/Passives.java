package me.numin.spirits.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;

public class Passives implements Listener {

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Element spirit = Element.getElement("Spirit");
            Player player = (Player) event.getEntity();
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

            if (event.isCancelled() || bPlayer == null || bPlayer.hasElement(Element.AIR) || bPlayer.hasElement(Element.EARTH)) return;

            if (bPlayer.hasElement(spirit) && event.getCause() == DamageCause.FALL) {
                event.setDamage(0D);
                event.setCancelled(true);

            }
        }
    }

}