package com.gamma.hub.events;

import com.gamma.hub.locale.Message;
import com.gamma.hub.menu.ServerMenu;
import com.gamma.hub.menu.SettingsMenu;
import com.pepej.papi.checker.checker.nullness.qual.NonNull;
import com.pepej.papi.events.Events;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.gamma.hub.items.HubItems.*;


public class EventListener implements TerminableModule {

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerInteractEvent.class)
              .filter(e -> e.hasItem() && e.getItem().isSimilar(COMPASS))
              .handler(e -> new ServerMenu(e.getPlayer()).open())
              .bindWith(consumer);
        Events.subscribe(PlayerInteractEvent.class)
              .filter(e -> e.hasItem() && e.getItem().isSimilar(COMPARATOR))
              .handler(e -> new SettingsMenu(e.getPlayer()).open())
              .bindWith(consumer);
 Events.subscribe(PlayerInteractAtEntityEvent.class)
              .filter(e -> e.getPlayer().getInventory().getItemInMainHand() != null && e.getPlayer().getInventory().getItemInMainHand().isSimilar(LEASH))
              .handler(e -> {

              })
              .bindWith(consumer);

        Events.subscribe(PlayerJoinEvent.class)
              .handler(e -> {
                  e.getPlayer().getInventory().setItem(0, COMPASS);
                  e.getPlayer().getInventory().setItem(2, COMPARATOR);
                  e.getPlayer().getInventory().setItem(4, LEASH);
                  Message.JOIN.send(e.getPlayer(), e.getPlayer());
              })
              .bindWith(consumer);
    }
}
