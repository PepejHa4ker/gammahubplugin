package com.gamma.hub.menu;

import com.gamma.hub.locale.Message;
import com.gamma.hub.metadata.PlayersMetadata;
import com.pepej.papi.item.ItemStackBuilder;
import com.pepej.papi.menu.Item;
import com.pepej.papi.menu.Menu;
import com.pepej.papi.menu.scheme.MenuPopulator;
import com.pepej.papi.menu.scheme.MenuScheme;
import com.pepej.papi.menu.scheme.StandardSchemeMappings;
import com.pepej.papi.metadata.Metadata;
import com.pepej.papi.metadata.MetadataMap;
import com.pepej.papi.utils.Players;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class SettingsMenu extends Menu {

    private static final MenuScheme SCHEME = new MenuScheme()
            .maskEmpty(1)
            .mask("001010100")
            .maskEmpty(1);

    public SettingsMenu(final Player player) {
        super(player, 3, "Настройки личного кабинета");
    }

    @Override
    public void redraw() {
        fillNullableWith(StandardSchemeMappings.STAINED_GLASS.get(7).get());
        MenuPopulator populator = SCHEME.newPopulator(this);
        Item hideItem = ItemStackBuilder.of(Material.REDSTONE_COMPARATOR)
                .nameClickable("&cСкрыть&7/&aПоказать&7 игроков в хабе")
                .buildConsumer(e -> {
                    Player player = (Player) e.getWhoClicked();

                    MetadataMap metadataMap = Metadata.provideForPlayer(player);
                    Boolean playersHidden = metadataMap.getOrNull(PlayersMetadata.PLAYERS_HIDDEN);
                    if (playersHidden == null) {
                        metadataMap.forcePut(PlayersMetadata.PLAYERS_HIDDEN, true);
                        playersHidden = true;
                    }
                    Message.PLAYERS_TOGGLE.send(player, playersHidden);
                    metadataMap.forcePut(PlayersMetadata.PLAYERS_HIDDEN, !playersHidden);
                    if (playersHidden) {
                        Players.all().forEach(player::hidePlayer);
                    } else {
                        Players.all().forEach(player::showPlayer);
                    }
                });
        Item stackingItem = ItemStackBuilder.of(Material.LEASH)
                                        .nameClickable("&cВключить&7/&aВыключить&7 стакинг")
                                        .buildConsumer(e -> {
                                            Player player = (Player) e.getWhoClicked();

                                            MetadataMap metadataMap = Metadata.provideForPlayer(player);
                                            Boolean stackingEnabled = metadataMap.getOrNull(PlayersMetadata.STACKING_TOGGLED);
                                            if (stackingEnabled == null) {
                                                metadataMap.forcePut(PlayersMetadata.STACKING_TOGGLED, true);
                                                stackingEnabled = true;
                                            }
                                            Message.STACKING_TOGGLE.send(player, stackingEnabled);
                                            metadataMap.forcePut(PlayersMetadata.STACKING_TOGGLED, !stackingEnabled);
                                            if (!stackingEnabled) {
                                                Entity passenger = player.getPassengers().isEmpty() ? null : player.getPassengers().get(0);
                                                if (passenger != null) {
                                                    passenger.removePassenger(passenger);
                                                }
                                            }
                                        });
        populator.accept(hideItem);
        populator.accept(stackingItem);
    }
}
