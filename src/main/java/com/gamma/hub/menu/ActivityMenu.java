package com.gamma.hub.menu;

import com.gamma.hub.database.DatabaseAdapter;
import com.pepej.papi.item.ItemStackBuilder;
import com.pepej.papi.menu.paginated.PaginatedMenu;
import com.pepej.papi.menu.paginated.PaginatedMenuBuilder;
import com.pepej.papi.menu.scheme.MenuScheme;
import com.pepej.papi.menu.scheme.StandardSchemeMappings;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ActivityMenu extends PaginatedMenu {


    private static final List<Integer> ITEM_SLOTS = MenuScheme.create()
                                                              .maskEmpty(1)
                                                              .mask("011111110")
                                                              .maskEmpty(1)
                                                              .getMaskedIndexes();
    private static final int NEXT_PAGE_SLOT = MenuScheme.create()
                                                        .maskEmpty(2)
                                                        .mask("000000010")
                                                        .getMaskedIndexes()
                                                        .get(0);

    private static final int PREVIOUS_PAGE_SLOT = MenuScheme.create()
                                                            .maskEmpty(2)
                                                            .mask("010000000")
                                                            .getMaskedIndexes()
                                                            .get(0);


    public ActivityMenu(List<DatabaseAdapter.JoinData> joinData, final Player player) {
        super($ -> {
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ROOT);
                    return joinData.stream()

                                   .sorted(Comparator.comparingLong(DatabaseAdapter.JoinData::getJoinTs).reversed())
                                   .map(data -> ItemStackBuilder.of(Material.BOOK)
                                                                .name(format("&a%s", dateFormat.format(new Date(data.getJoinTs()))))
                                                                .lore(format("&7Ip: &e%s", data.getIp()))
                                                                .buildItem()
                                                                .build())
                                   .collect(Collectors.toList());

                }, player, PaginatedMenuBuilder.create()
                                               .scheme(MenuScheme.create(StandardSchemeMappings.EMPTY))
                                               .itemSlots(ITEM_SLOTS)
                                               .lines(3)
                                               .nextPageSlot(NEXT_PAGE_SLOT)
                                               .previousPageSlot(PREVIOUS_PAGE_SLOT)
                                               .title("История заходов")


        );
    }
}
