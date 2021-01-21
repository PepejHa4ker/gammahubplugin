package com.gamma.hub.model;


import com.pepej.papi.checker.checker.nullness.qual.NonNull;
import com.pepej.papi.google.common.collect.Sets;
import com.pepej.papi.google.gson.JsonElement;
import com.pepej.papi.gson.GsonSerializable;
import com.pepej.papi.gson.JsonBuilder;
import com.pepej.papi.utils.UndashedUuids;
import lombok.Data;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Data
public class User implements GsonSerializable {

    @Getter
    private static final Set<User> users = Sets.newHashSet();

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
