package com.gamma.hub.model;

import com.pepej.papi.Services;
import com.pepej.papi.config.ConfigFactory;
import com.pepej.papi.config.configurate.ConfigurationNode;
import com.pepej.papi.google.common.collect.Lists;
import com.pepej.papi.messaging.bungee.BungeeCord;
import com.pepej.papi.scheduler.Schedulers;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class ServerInfo {

    private final String id;
    private final String name;
    private final Material material;
    private final String[] description;
    private final boolean arcades;
    @Getter
    private static final List<ServerInfo> servers = Lists.newArrayList();
    private int online;
    private static boolean updating;

    public ServerInfo( final String id,  final String name,  final Material material, final boolean arcades, final  String... description) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.arcades = arcades;
        this.description = description;
        if (!updating) {
            startUpdateTask();
            updating = true;

        }

    }
    private static void startUpdateTask() {
        BungeeCord bungee = Services.load(BungeeCord.class);
        Schedulers.builder()
                  .async()
                  .every(5, TimeUnit.SECONDS)
                  .run(() -> {
                      for (ServerInfo server : servers) {
                         bungee.playerCount(server.getId()).thenAcceptAsync(server::setOnline);
                      }
                  });
    }

    public static void load(File file) {
        ConfigurationNode config = ConfigFactory.gson().load(file);
        config.getChildrenList().forEach(server -> {
            ServerInfo loaded = new ServerInfo(
                    server.getNode("id").getString(),
                    server.getNode("name").getString(),
                    Material.valueOf(server.getNode("material").getString()),
                    server.getNode("isArcade").getBoolean(),
                    server.getNode("description").getChildrenList()
                                   .stream()
                                   .map(ConfigurationNode::getString)
                                   .toArray(String[]::new));

            servers.add(loaded);

        });
    }
}
