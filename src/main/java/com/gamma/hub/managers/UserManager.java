package com.gamma.hub.managers;

import com.gamma.hub.model.User;

import com.pepej.papi.events.Events;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;
import com.pepej.papi.utils.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;


public class UserManager implements TerminableModule {


    @Nullable
    public User getUser(final Player player) {
        return User.getUsers().stream().filter(user -> user.getId().equals(player.getUniqueId())).findAny().orElse(null);
    }

    public UserManager() {
        loadAll();
    }


    private void loadAll() {
        Players.all().forEach(this::loadUser);
    }

    private User loadUser(final Player player) {
        User user = new User(player.getUniqueId(), player.getName());
        User.getUsers().add(user);
        return user;
    }


    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerLoginEvent.class)
              .handler(e -> loadUser(e.getPlayer()))
              .bindWith(consumer);

        Events.merge(PlayerEvent.class, PlayerQuitEvent.class, PlayerKickEvent.class)
              .handler(e -> {
                  final User user = getUser(e.getPlayer());
                  if (user != null) {
                      user.unload();
                  }
              })
              .bindWith(consumer);
    }
}
