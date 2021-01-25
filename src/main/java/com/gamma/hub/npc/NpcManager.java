package com.gamma.hub.npc;

import com.gamma.hub.model.ServerInfo;
import com.pepej.papi.Services;
import com.pepej.papi.checker.checker.nullness.qual.NonNull;
import com.pepej.papi.messaging.bungee.BungeeCord;
import com.pepej.papi.npc.CitizensNpc;
import com.pepej.papi.npc.CitizensNpcFactory;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;

import static java.util.stream.Collectors.toList;

public class NpcManager implements TerminableModule {
    @Override
    public void setup(@NonNull TerminableConsumer terminableConsumer) {
        CitizensNpcFactory npcFactory = Services.load(CitizensNpcFactory.class);
        for (ServerInfo server : ServerInfo.getServers().stream().filter(s -> !s.isArcades()).collect(toList())) {
            final CitizensNpc npc = npcFactory.spawnNpc(server.getNpcPosition().toLocation(), server.getName(), server.getNpcTexture(), server.getNpcSignature());
            npc.setClickCallback(server::connectPlayer);
        }
    }
}
