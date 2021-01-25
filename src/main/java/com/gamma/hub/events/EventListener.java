package com.gamma.hub.events;

import com.gamma.hub.locale.Message;
import com.gamma.hub.menu.ServerMenu;
import com.gamma.hub.menu.SettingsMenu;
import com.pepej.papi.checker.checker.nullness.qual.NonNull;
import com.pepej.papi.events.Events;
import com.pepej.papi.scheduler.Schedulers;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;
import com.pepej.papi.utils.Players;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

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
                .filter(e -> {
                    if (e.getHand() != EquipmentSlot.HAND) return false;
                    if (!(e.getRightClicked() instanceof Player)) return false;
                    ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                    return item != null && item.equals(LEASH);
                })
                .handler(e -> {
                    Player rightClicked = (Player) e.getRightClicked();
                    List<Entity> passengers = e.getPlayer().getPassengers();
                    if (passengers.size() != 0) {
                        Entity passenger = passengers.get(0);
                        if (passenger.equals(rightClicked)) return;
                        passenger.addPassenger(rightClicked);
                        return;
                    }
                    e.getPlayer().addPassenger(rightClicked);
                })
                .bindWith(consumer);
        Events.subscribe(PlayerInteractEvent.class)
                .filter(e -> !e.getPlayer().getPassengers().isEmpty() && e.getPlayer().isSneaking())
                .handler(e -> {
                    Player player = e.getPlayer();
                    Player passenger = ((Player) player.getPassengers().get(0));
                    player.removePassenger(passenger);
                    Location location = player.getLocation();
                    Vector direction = location.getDirection();
                    Vector newPoint = direction.multiply(2);
                    passenger.setVelocity(newPoint);
                    Players.playSound(player, Sound.ENTITY_FIREWORK_LAUNCH);
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
