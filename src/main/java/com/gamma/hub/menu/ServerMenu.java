package com.gamma.hub.menu;

import com.gamma.hub.model.Head;
import com.gamma.hub.model.ServerInfo;
import com.pepej.papi.Services;
import com.pepej.papi.item.ItemStackBuilder;
import com.pepej.papi.menu.Menu;
import com.pepej.papi.menu.scheme.MenuPopulator;
import com.pepej.papi.menu.scheme.MenuScheme;
import com.pepej.papi.menu.scheme.StandardSchemeMappings;
import com.pepej.papi.messaging.bungee.BungeeCord;
import com.pepej.papi.scheduler.Schedulers;
import com.pepej.papi.utils.Players;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class ServerMenu extends Menu {
    private static final MenuScheme SERVER_SCHEME =  new MenuScheme()
            .maskEmpty(1)
            .mask("001000100")
            .mask("000010000")
            .mask("001000100")
            .mask("000000000")
            .mask("010000010");

    private int index;

    public ServerMenu(Player player) {
        super(player, 6, "&l&7[&l&cМеню &l&6режимов&l&7]");
        Schedulers.sync()
                  .runRepeating(this::redraw,0,20)
                  .bindWith(this);

    }

    @Override
    @SneakyThrows
    public void redraw() {
        index = index == 15 ? 1 : index == 8 ? 9 : index + 1;
        fillWith(StandardSchemeMappings.STAINED_GLASS.get(index).get());
        BungeeCord bungee = Services.load(BungeeCord.class);
        MenuPopulator serverPopulator = SERVER_SCHEME.newPopulator(this);
        for (ServerInfo server : ServerInfo.getServers().stream().filter(s -> !s.isArcades()).collect(Collectors.toList())) {
            serverPopulator.accept(ItemStackBuilder.of(server.getMaterial())
                                                   .nameClickable(server.getName())
                                                   .enchant(Enchantment.ARROW_DAMAGE,1)
                                                   .hideAttributes()
                                                   .lore(String.format("&aИгроков онлайн: &d%s", server.getOnline()))
                                                   .lore(server.getDescription())
                                                   .loreClickable("подключиться к серверу")
                                                   .buildConsumer(e -> {
                                                       Player player = (Player) e.getWhoClicked();
                                                       bungee.connect(getPlayer(), server.getId());
                                                       Players.playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
                                                   }));
            this.setItem(40, ItemStackBuilder.of(Material.COOKIE)
                                             .nameClickable("&3Аркады")
                                             .loreClickable("открыть меню аркад")
                                             .buildConsumer(e -> {
                                                 Player player = (Player) e.getWhoClicked();
                                                 new ArcadesMenu((Player) e.getWhoClicked()).open();
                                                 Players.playSound(player, Sound.UI_BUTTON_CLICK);
                                             }));
        }

    }

    private static class ArcadesMenu extends Menu {

        private static final MenuScheme SERVERS_SCHEME = new MenuScheme()
                .maskEmpty(1)
                .mask("001010100")
                .maskEmpty(1);

        public ArcadesMenu(Player player) {
            super(player, 3, "Мини игры");

        }


        @Override
        public void redraw() {
            BungeeCord bungee = Services.load(BungeeCord.class);
            MenuPopulator serversPopulator = SERVERS_SCHEME.newPopulator(this);
            this.setItem(0,ItemStackBuilder.head(Head.findByName("Red Arrow Left").getTexture())
                                           .nameClickable("&cНазад")
                                           .buildConsumer(e -> {
                                               Player player = (Player) e.getWhoClicked();
                                               new ServerMenu(player).open();
                                           }));


            for (ServerInfo server : ServerInfo.getServers().stream().filter(ServerInfo::isArcades).collect(Collectors.toList())) {
                serversPopulator.accept(ItemStackBuilder.of(server.getMaterial())
                                                        .nameClickable(server.getName())
                                                        .enchant(Enchantment.ARROW_DAMAGE)
                                                        .hideAttributes()
                                                        .lore(String.format("&aИгроков онлайн: &d%s", server.getOnline()))
                                                        .lore(server.getDescription())
                                                        .loreClickable("подключиться к серверу")
                                                        .buildConsumer(e -> {
                                                            Player player = (Player) e.getWhoClicked();
                                                            bungee.connect(player, server.getId());
                                                            Players.playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
                                                        }));

            }
        }
    }


}

