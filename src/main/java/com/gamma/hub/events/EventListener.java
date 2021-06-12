package com.gamma.hub.events;

import com.gamma.hub.database.DatabaseAdapter;
import com.gamma.hub.locale.Message;
import com.gamma.hub.menu.ServerMenu;
import com.gamma.hub.metadata.PlayersMetadata;
import com.pepej.papi.cooldown.Cooldown;
import com.pepej.papi.cooldown.CooldownMap;
import com.pepej.papi.events.Events;
import com.pepej.papi.metadata.Metadata;
import com.pepej.papi.metadata.MetadataMap;
import com.pepej.papi.npc.NpcFactory;
import com.pepej.papi.scheduler.Schedulers;
import com.pepej.papi.services.Services;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;
import com.pepej.papi.text.Text;
import com.pepej.papi.utils.Log;
import com.pepej.papi.utils.Players;
import com.pepej.papi.utils.entityspawner.EntitySpawner;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gamma.hub.items.HubItems.*;


public class EventListener implements TerminableModule, Listener {

    private final NpcFactory npcFactory;
    private final DatabaseAdapter adapter;
    private final CooldownMap<Player> cooldownMap;

    public EventListener() {
        this.npcFactory = Services.getNullable(NpcFactory.class);
        this.adapter = Services.getNullable(DatabaseAdapter.class);
        this.cooldownMap = CooldownMap.create(Cooldown.of(5, TimeUnit.SECONDS));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().equals(COMPASS)) {
            if (cooldownMap.test(e.getPlayer())) {
                new ServerMenu(e.getPlayer()).open();

            } else {
                Players.msg(e.getPlayer(), "&7Подождите. Не так быстро");
            }
        }
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {

        Events.subscribe(PlayerInteractAtEntityEvent.class)
                .filter(e -> {
                    if (npcFactory.isPapiNPC(e.getRightClicked())) return false;
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
                    adapter.insertJoinData(e.getPlayer());
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000000, 2, true, true));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 1, true, true));
                    if (e.getPlayer().isOp()) {
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 1, true, true));

                    }
                    Schedulers.sync().runLater(() -> {
                        EntitySpawner.INSTANCE.spawn(e.getPlayer().getLocation(), Firework.class, fw -> {
                                    FireworkMeta fireworkMeta = fw.getFireworkMeta();
                                    fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.LIME).trail(true).build());
                                    fw.setFireworkMeta(fireworkMeta);
                        });
                        e.getPlayer().getInventory().setItem(0, COMPASS);
                        e.getPlayer().getInventory().setItem(4, LEASH);
                    }, 1, TimeUnit.SECONDS);

                    Message.JOIN.send(e.getPlayer(), e.getPlayer());
                })
                .bindWith(consumer);

    }
}
