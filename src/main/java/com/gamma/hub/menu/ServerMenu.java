package com.gamma.hub.menu;

import com.gamma.hub.database.DatabaseAdapter;
import com.gamma.hub.model.Head;
import com.gamma.hub.model.ServerInfo;
import com.pepej.papi.item.ItemStackBuilder;
import com.pepej.papi.menu.Menu;
import com.pepej.papi.menu.scheme.MenuPopulator;
import com.pepej.papi.menu.scheme.MenuScheme;
import com.pepej.papi.messaging.bungee.BungeeCord;
import com.pepej.papi.scheduler.Schedulers;
import com.pepej.papi.services.Services;
import com.pepej.papi.utils.Players;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class ServerMenu extends Menu {
//    private static final MenuScheme SERVER_SCHEME = MenuScheme.create()
//                                                              .mask("000000000")
//                                                              .mask("001000100")
//                                                              .mask("000010000")
//                                                              .mask("001010100")
//                                                              .mask("000000000")
//                                                              .mask("010000010");

    private static final MenuScheme BUILD_SERVER_SCHEME = MenuScheme.create()
                                                                    .mask("000000001")
                                                                    .maskEmpty(5);

    private final DatabaseAdapter adapter;

    public ServerMenu(Player player) {
        super(player, 6, "&eРежимы");
        adapter = Services.getNullable(DatabaseAdapter.class);
    }

    @Override
    @SneakyThrows
    public void redraw() {
        BungeeCord bungeeCord = Services.getNullable(BungeeCord.class);
//        MenuPopulator serverPopulator = SERVER_SCHEME.newPopulator(this);
        MenuPopulator buildPopulator = BUILD_SERVER_SCHEME.newPopulator(this);
        if (getPlayer().hasPermission("bs.adm")) {
            buildPopulator.accept(
                    ItemStackBuilder.of(Material.EMERALD_BLOCK)
                                    .nameClickable("&aБилд сервер")
                                    .loreClickable("подключиться к билд серверу")
                                    .buildConsumer(e -> bungeeCord.connect((Player) e.getWhoClicked(), "build"))
            );
        }
        setItem(4, ItemStackBuilder.of(Material.COOKIE)
                                   .nameClickable("&3Аркады")
                                   .loreClickable("открыть меню аркад")
                                   .buildConsumer(e -> {
                                       Player player = (Player) e.getWhoClicked();
                                       new ArcadesMenu(player).open();
                                       Players.playSound(player, Sound.UI_BUTTON_CLICK);
                                   }));
        ServerInfo.getServers().stream().filter(s -> !s.isArcade()).forEach(server -> {
            Schedulers.async().run(() -> populate(this, adapter, server, getPlayer()));
        });


    }


    public static class ArcadesMenu extends Menu {

        private static final MenuScheme SERVERS_SCHEME = MenuScheme.create()
                                                                   .maskEmpty(1)
                                                                   .mask("001010100")
                                                                   .mask("000101000")
                                                                   .maskEmpty(1);

        private final DatabaseAdapter adapter;


        public ArcadesMenu(Player player) {
            super(player, 4, "Мини игры");
            this.adapter = Services.getNullable(DatabaseAdapter.class);

        }


        @Override
        public void redraw() {
//            MenuPopulator serversPopulator = SERVERS_SCHEME.newPopulator(this);
            this.setItem(0, ItemStackBuilder.head(Head.findByName("Red Arrow Left").getTexture())
                                            .nameClickable("&cНазад")
                                            .buildConsumer(e -> {
                                                Player player = (Player) e.getWhoClicked();
                                                new ServerMenu(player).open();
                                            }));


            ServerInfo.getServers().stream().filter(ServerInfo::isArcade).forEach(server -> {
                Schedulers.async().run(() -> populate(this, adapter, server, getPlayer()));
            });


        }

    }


    private static List<DatabaseAdapter.ServerJoinData> getDataForADays(List<DatabaseAdapter.ServerJoinData> data, int days) {
        return data.stream()
                   .filter(d -> (System.currentTimeMillis() - d.getJoinTs()) <= Duration.ofDays(days).toMillis())
                   .collect(toList());
    }

    private static void populate(Menu menu, DatabaseAdapter adapter, ServerInfo server, Player player) {
        adapter.fetchServerJoinData(server.getId())
               .thenApplyAsync(data -> {
                   ItemStackBuilder builder = ItemStackBuilder.of(server.getMaterial())
                                                              .nameClickable(server.getName())
                                                              .data(server.getMaterialData())
                                                              .enchant(Enchantment.ARROW_DAMAGE, 1)
                                                              .hideAttributes()
                                                              .lore("")
                                                              .lore(format("&aИгроков онлайн: &d%s", server.getOnline()))
                                                              .lore(server.getDescription())
                                                              .loreClickable("подключиться к серверу");
                   if (player.hasPermission("servers.activity." + server.getId())) {
                       builder.lore(format("&7Подключений за день: &a%s", getDataForADays(data, 1).size()));
                       builder.lore(format("&7Подключений за неделю: &a%s", getDataForADays(data, 7).size()));
                       builder.lore(format("&7Подключений за месяц: &a%s", getDataForADays(data, 31).size()));


                   }
                   return builder;

               })
               .thenAcceptSync(builder -> {
                   menu.setItem(server.getMenuId(), builder.buildConsumer(e -> {
                       server.connectPlayer(player);
                       Players.playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
                   }));
               });
    }


}





