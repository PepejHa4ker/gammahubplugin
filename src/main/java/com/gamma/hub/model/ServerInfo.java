package com.gamma.hub.model;

import com.pepej.papi.Services;
import com.pepej.papi.config.ConfigFactory;
import com.pepej.papi.config.configurate.ConfigurationNode;
import com.pepej.papi.google.common.collect.Lists;
import com.pepej.papi.gson.GsonProvider;
import com.pepej.papi.messaging.bungee.BungeeCord;
import com.pepej.papi.scheduler.Schedulers;
import com.pepej.papi.serialize.Position;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

@Data
public class ServerInfo {

    private final String id;
    private final String name;
    private final Material material;
    private final String[] description;
    private final boolean arcades;
    private final Position npcPosition;
    private final String npcTexture;
    private final String npcSignature;
    @Getter
    private static final List<ServerInfo> servers = Lists.newArrayList();
    private int online;
    private static boolean updating;

    public ServerInfo(final String id, final String name, final Material material, final boolean arcades, final Position npcPosition, final String npcTexture, final String npcSignature, final String... description) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.arcades = arcades;
        this.npcPosition = npcPosition;
        this.npcTexture = npcTexture;
        this.npcSignature = npcSignature;
        this.description = description;
        if (!updating) {
            startUpdateTask();
            updating = true;

        }

    }


    public void connectPlayer(Player player) {
        BungeeCord bungeeCord = Services.load(BungeeCord.class);
        bungeeCord.connect(player, this.id);
    }

    private static void startUpdateTask() {
        BungeeCord bungee = Services.load(BungeeCord.class);
        Schedulers.builder()
                  .async()
                  .afterAndEvery(5, TimeUnit.SECONDS)
                  .run(() -> servers.forEach(server -> bungee.playerCount(server.getId()).thenAcceptAsync(server::setOnline)));
    }

    public static void load(File file) {
        ConfigurationNode config = ConfigFactory.gson().load(file);
        config.getChildrenList().forEach(server -> {
            ConfigurationNode npc  = server.getNode("npc");
            ServerInfo loaded = new ServerInfo(
                    server.getNode("id").getString(),
                    server.getNode("name").getString(),
                    Material.valueOf(server.getNode("material").getString()),
                    server.getNode("isArcade").getBoolean(),
                    Position.deserialize(GsonProvider.parser().parse(npc.getNode("position").getString())),
                    npc.getNode("texture").getString(),
                    npc.getNode("signature").getString(),
                    server.getNode("description").getChildrenList()
                                                   .stream()
                                                   .map(ConfigurationNode::getString)
                                                   .toArray(String[]::new));

            servers.add(loaded);

        });
    }
}
