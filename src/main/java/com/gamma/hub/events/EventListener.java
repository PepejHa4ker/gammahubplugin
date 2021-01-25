package com.gamma.hub.events;

import com.gamma.hub.locale.Message;
import com.gamma.hub.menu.ServerMenu;
import com.gamma.hub.menu.SettingsMenu;
import com.pepej.papi.Papi;
import com.pepej.papi.checker.checker.nullness.qual.NonNull;
import com.pepej.papi.events.Events;
import com.pepej.papi.scheduler.Schedulers;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;
import com.pepej.papi.utils.Players;
import com.pepej.papi.utils.entityspawner.EntitySpawner;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000000, 2, true, true));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 1, true, true));
                    if (e.getPlayer().isOp()) {
                        Papi.executeCommand("vanish " + e.getPlayer().getName());
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 1, true, true));

                    }
                    Schedulers.sync().runLater(() -> {
                        EntitySpawner.INSTANCE.spawn(e.getPlayer().getLocation(), Firework.class, fw -> {
                                    FireworkMeta fireworkMeta = fw.getFireworkMeta();
                                    fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.LIME).trail(true).build());
                                    fw.setFireworkMeta(fireworkMeta);
                        });
                        e.getPlayer().getInventory().setItem(0, COMPASS);
                        e.getPlayer().getInventory().setItem(2, COMPARATOR);
                        e.getPlayer().getInventory().setItem(4, LEASH);
                    }, 1, TimeUnit.SECONDS);

                    Message.JOIN.send(e.getPlayer(), e.getPlayer());
                })
                .bindWith(consumer);

    }
}
