package com.gamma.hub.menu;

import com.gamma.hub.model.DonatInfo;
import com.pepej.papi.item.ItemStackBuilder;
import com.pepej.papi.menu.Menu;
import com.pepej.papi.menu.scheme.MenuPopulator;
import com.pepej.papi.menu.scheme.MenuScheme;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class DonatMenu extends Menu {
    private static final MenuScheme DONAT_SCHEME = new MenuScheme()
            .maskEmpty(1)
            .mask("001111100")
            .maskEmpty(1);

    public DonatMenu(Player player) {
        super(player, 3, "Плюшки для донатов");
    }




    @Override
    public void redraw() {
        MenuPopulator donatPopulator = DONAT_SCHEME.newPopulator(this);
        for (DonatInfo donat : DonatInfo.getDonats()) {
            donatPopulator.accept(ItemStackBuilder.of(donat.getMaterial())
                    .nameClickable(donat.getName())
                    .enchant(Enchantment.ARROW_DAMAGE)
                    .hideAttributes()
                    .lore(donat.getDescription())
                    .loreClickable("взять плюшку")
                    .buildConsumer(e -> {
                        Player player = (Player) e.getWhoClicked();

                    }));
        }

    }

}
