package com.gamma.hub.model;

import com.gamma.hub.database.DatabaseAdapter;
import com.google.common.collect.Lists;
import com.pepej.papi.config.ConfigFactory;
import com.pepej.papi.config.ConfigurationNode;
import com.pepej.papi.config.objectmapping.ConfigSerializable;
import com.pepej.papi.config.objectmapping.meta.Setting;
import com.pepej.papi.events.Events;
import com.pepej.papi.gson.GsonProvider;
import com.pepej.papi.messaging.bungee.BungeeCord;
import com.pepej.papi.scheduler.Schedulers;
import com.pepej.papi.serialize.Position;
import com.pepej.papi.services.Services;
import com.pepej.papi.terminable.Terminable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigSerializable
public class ServerInfo {

    @Setting int menuId;
    @Setting String id;
    @Setting String name;
    @Setting Material material;
    @Setting int materialData;
    @Setting String[] description;
    @Setting boolean arcade;
    @Setting NpcConfig npc;
    @Getter
    private static final List<ServerInfo> servers = Lists.newArrayList();
    private int online;
    private static boolean updating;

    public ServerInfo(final int menuId, final String id, final String name, final Material material, final int materialData, final boolean arcade, final NpcConfig npc, final String... description) {
        this.menuId = menuId;
        this.id = id;
        this.name = name;
        this.material = material;
        this.arcade = arcade;
        this.materialData = materialData;
        this.npc = npc;
        this.description = description;

    }


    public void connectPlayer(Player player) {
        DatabaseAdapter adapter = Services.getNullable(DatabaseAdapter.class);
        BungeeCord bungeeCord = Services.getNullable(BungeeCord.class);
        bungeeCord.connect(player, this.id);
        adapter.insertServerData(player, this.id);
    }

    private static void startUpdateTask() {
        BungeeCord bungee = Services.getNullable(BungeeCord.class);
        Schedulers.builder()
                  .async()
                  .afterAndEvery(5, TimeUnit.SECONDS)
                  .run(() -> servers.forEach(server -> bungee.playerCount(server.getId()).thenAcceptAsync(server::setOnline)));
    }

    @SneakyThrows
    public static void load(File file) {
        ConfigurationNode config = ConfigFactory.gson().load(file);
        final List<ServerInfo> servers = config.getList(ServerInfo.class);
        if (servers == null) return;

        ServerInfo.servers.addAll(servers);
        if (!updating) {
            startUpdateTask();
            updating = true;

        }


    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ConfigSerializable
    public static class NpcConfig {

        @Setting
        Position position;
        @Setting
        String texture;
        @Setting
        String signature;
    }
}
