package com.gamma.hub.model;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pepej.papi.gson.GsonSerializable;
import com.pepej.papi.gson.JsonBuilder;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.NonNull;

@Data
public class User implements GsonSerializable {

    private boolean isPlayerHidden;
    private boolean isStackingEnabled;

    @Override
    public @NonNull JsonElement serialize() {
        return JsonBuilder.object()
                .add("isPlayerHidden", isPlayerHidden)
                .add("isStackingEnabled", isStackingEnabled)
                .build();
    }
}
