package com.gamma.hub.items;

import com.pepej.papi.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public final class HubItems {

    public static final ItemStack COMPASS = ItemStackBuilder.of(Material.COMPASS)
                                                             .nameClickable("&dНавигатор")
                                                             .hideAttributes()
                                                             .enchant(Enchantment.ARROW_DAMAGE)
                                                             .build();
    public static final ItemStack COMPARATOR = ItemStackBuilder.of(Material.REDSTONE_COMPARATOR)
                                                                .nameClickable("&6Настройщик")
                                                                .hideAttributes()
                                                                .enchant(Enchantment.ARROW_DAMAGE)
                                                                .build();

    public static final ItemStack LEASH = ItemStackBuilder.of(Material.LEASH)
            .nameClickable("&dБашня")
            .hideAttributes()
            .build();
}
