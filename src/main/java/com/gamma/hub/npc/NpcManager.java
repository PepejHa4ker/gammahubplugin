package com.gamma.hub.npc;

import com.pepej.papi.Services;
import com.pepej.papi.checker.checker.nullness.qual.NonNull;
import com.pepej.papi.npc.CitizensNpcFactory;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;

public class NpcManager implements TerminableModule {
    @Override
    public void setup(@NonNull TerminableConsumer terminableConsumer) {
        CitizensNpcFactory npcFactory = Services.load(CitizensNpcFactory.class);
        npcFactory.spawnNpc()
    }
}
