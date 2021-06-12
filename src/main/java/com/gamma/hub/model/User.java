package com.gamma.hub.model;


import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.pepej.papi.cooldown.Cooldown;
import com.pepej.papi.cooldown.CooldownMap;
import com.pepej.papi.gson.GsonSerializable;
import com.pepej.papi.gson.JsonBuilder;
import com.pepej.papi.utils.UndashedUuids;
import lombok.Data;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
public class User implements GsonSerializable {

    @Getter
    private static final Set<User> users = Sets.newHashSet();

    private CooldownMap<DonatInfo> cooldown = CooldownMap.create(Cooldown.of(1, TimeUnit.DAYS));
    private final UUID id;
    private final String username;
    private boolean isPlayerHidden;
    private boolean isStackingEnabled;


    @Override
    public @NonNull JsonElement serialize() {
        return JsonBuilder.object()
                          .add("id", UndashedUuids.toString(id))
                          .add("username", username)
                          .add("isPlayerHidden", isPlayerHidden)
                          .add("isStackingEnabled", isStackingEnabled)
                          .build();
    }

    public void unload() {
        users.remove(this);
    }
}
