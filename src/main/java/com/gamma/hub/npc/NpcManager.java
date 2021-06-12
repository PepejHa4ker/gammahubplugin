package com.gamma.hub.npc;

import com.gamma.hub.menu.DonatMenu;
import com.gamma.hub.menu.ServerMenu.ArcadesMenu;
import com.gamma.hub.model.ServerInfo;
import com.pepej.papi.Papi;
import com.pepej.papi.npc.CitizensNpc;
import com.pepej.papi.npc.CitizensNpcFactory;
import com.pepej.papi.services.Services;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;


public class NpcManager implements TerminableModule {
    @Override
    public void setup(@NonNull TerminableConsumer terminableConsumer) {
        CitizensNpcFactory npcFactory = Services.getNullable(CitizensNpcFactory.class);
        ServerInfo.getServers().stream().filter(s -> !s.isArcade()).forEach(server -> {
            final ServerInfo.NpcConfig serverNpc = server.getNpc();
            final CitizensNpc npc = npcFactory.spawnNpc(serverNpc.getPosition().toLocation(), server.getName(), serverNpc.getTexture(), serverNpc.getSignature());
            npc.setClickCallback(server::connectPlayer);
        });


        final CitizensNpc npc2 = npcFactory.spawnNpc(new Location(Papi.worldNullable("hub"), 160.5, 91, 13.5), "&dДонат", "ewogICJ0aW1lc3RhbXAiIDogMTYxMTkyNDQyNzgzMSwKICAicHJvZmlsZUlkIiA6ICJjYzg2YWFlNTgxMDk0MzIwYTM3MDRkN2NjMTY3YTNmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBenVsVmFsZGV6IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUzOWM4MTViMjdkYzkyNjQ0Y2RhMzk0NWEzNjgxNmFlMDdhN2M1ZDkwYWI1N2RhMDRiZmQ5NjRkODkzOGUwMTciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==", "hiC1Wu4IqPOI2VpJgx+BoCuBf2gbt4uOxBWrJBZd/jDFEmnDHyyB8YDbjj6NmN7fviB6Rzp1hR5hyrqRUj5WsJ3LM3jVrBmwImKrEsLZqDnoi+AhZDCp+XvllghD5aUDTaKP6WO3bt1s7tbpF60eOn+UdQgSHwAjxC8bFPHMghodJIHRZGP/1Jq99Q8EkfAu76L6zpSicNM0+ib2DwU5NlF6U4hzeYYggPjfH4+cV+PeKkW1g6zH37+8/i5309sLH50Tl5j7mNF8nsQxuf7Dn6O+2EV27uHq1oqWS2N++kJMVr79TJK34rhNxRH+um0Rjc4pKEyCloi4p+xZyWxf8Ul4dz7WD5Wtl7tMTt4Jtn84WeeFDSzNINV7nUsdQhk5imjhmKo6yEjxRpjLWs/RzIOeCC5DcDDglFS6+RpePLP0PYQzb+iQZ89uLjDXP/67mK0XbmCPOJ8lb/LU0KuOiFdrpe2K55BwV7waum7UZrFenk/cQLgQAaJEZgu6ayULNBdUmw+kT9BK8+vYZdJsaqcOqPRLDRp3xDxXL9nEhCNodYae+CKyn29IphgHMz+EIVr37TpQmrqZFXAlgnWvv2521dOQlZajTbMA8QBpgjZEFXTky2t5HO3Us3ohZwd23BKptQipY17lmIaL37ux2k97DCO2fdPpe4iZ5YRqrO4=");
        npc2.setClickCallback(player ->
                new DonatMenu(player).open());
    }
}
