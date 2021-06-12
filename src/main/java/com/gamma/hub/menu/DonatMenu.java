package com.gamma.hub.menu;

import com.gamma.hub.locale.Message;
import com.gamma.hub.managers.UserManager;
import com.gamma.hub.model.DonatInfo;
import com.gamma.hub.model.User;
import com.pepej.papi.Papi;
import com.pepej.papi.item.ItemStackBuilder;
import com.pepej.papi.menu.Menu;
import com.pepej.papi.menu.scheme.MenuPopulator;
import com.pepej.papi.menu.scheme.MenuScheme;
import com.pepej.papi.services.Services;
import com.pepej.papi.utils.Players;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Locale;

public class DonatMenu extends Menu {
    private static final MenuScheme DONAT_SCHEME = MenuScheme.create()
            .maskEmpty(1)
            .mask("001111100")
            .maskEmpty(1);


    public DonatMenu(Player player) {
        super(player, 3, "&bПлюшки для донатов");
    }




    @Override
    public void redraw() {
        final UserManager userManager = Services.getNullable(UserManager.class);
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
                        User user = userManager.getUser(player);
                        if (user == null) {
                            return;
                        }
                        if (!player.hasPermission("donatmenu." + donat.getType().name().toLowerCase(Locale.ROOT))) {
                            Message.NO_PERMISSION_MESSAGE.send(player, "donatmenu." + donat.getType().name().toLowerCase(Locale.ROOT));
                            return;
                        }
                        if (!user.getCooldown().test(donat)) {
                            Message.BONUS_COOLDOWN_MESSAGE.send(player);
                            return;
                        }
                        Papi.executeCommand("eco give " + player.getName() + " " + (int) (500 * donat.getType().getWeight()));
                        Players.playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
                    }));
        }

    }

}
