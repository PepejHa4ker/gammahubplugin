package com.gamma.hub.commands;

import com.gamma.hub.locale.Message;
import com.gamma.hub.metadata.PlayersMetadata;
import com.pepej.papi.checker.checker.nullness.qual.NonNull;
import com.pepej.papi.command.Commands;
import com.pepej.papi.metadata.Metadata;
import com.pepej.papi.metadata.MetadataMap;
import com.pepej.papi.terminable.TerminableConsumer;
import com.pepej.papi.terminable.module.TerminableModule;
import com.pepej.papi.utils.Players;
import org.bukkit.entity.Entity;

import java.util.concurrent.TimeUnit;

public class GCommands implements TerminableModule {
    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Commands.create()
                .assertCooldown(1, TimeUnit.SECONDS, "Подождите &a{cooldown}&6 секунд")
                .assertPlayer()
                .handler(ctx -> {

                    MetadataMap metadataMap = Metadata.provideForPlayer(ctx.sender());
                    Boolean playersHidden = metadataMap.getOrNull(PlayersMetadata.PLAYERS_HIDDEN);
                    if (playersHidden == null) {
                        metadataMap.forcePut(PlayersMetadata.PLAYERS_HIDDEN, true);
                        playersHidden = true;
                    }
                    Message.PLAYERS_TOGGLE.send(ctx.sender(), playersHidden);
                    metadataMap.forcePut(PlayersMetadata.PLAYERS_HIDDEN, !playersHidden);
                    if (playersHidden) {
                        Players.all().forEach(ctx.sender()::hidePlayer);
                    } else {
                        Players.all().forEach(ctx.sender()::showPlayer);
                    }
                })
                .registerAndBind(consumer, "hideplayers");

        Commands.create()
                .assertCooldown(1, TimeUnit.SECONDS, "Подождите &a{cooldown}&6 секунд")
                .assertPlayer()
                .handler(ctx -> {

                    MetadataMap metadataMap = Metadata.provideForPlayer(ctx.sender());
                    Boolean stackingEnabled = metadataMap.getOrNull(PlayersMetadata.STACKING_TOGGLED);
                    if (stackingEnabled == null) {
                        metadataMap.forcePut(PlayersMetadata.STACKING_TOGGLED, true);
                        stackingEnabled = true;
                    }
                    Message.STACKING_TOGGLE.send(ctx.sender(), stackingEnabled);
                    metadataMap.forcePut(PlayersMetadata.PLAYERS_HIDDEN, !stackingEnabled);
                    if (!stackingEnabled) {
                        Entity passenger = ctx.sender().getPassengers().isEmpty() ? null : ctx.sender().getPassengers().get(0);
                        if (passenger != null) {
                            ctx.sender().removePassenger(passenger);
                        }
                    }
                })
                .registerAndBind(consumer, "stacking");
    }
}
