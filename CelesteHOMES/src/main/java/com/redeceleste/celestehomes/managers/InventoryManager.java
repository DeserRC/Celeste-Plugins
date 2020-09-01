package com.redeceleste.celestehomes.managers;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.events.InventoryEvent;
import com.redeceleste.celestehomes.models.InventoryArgument;
import com.redeceleste.celestehomes.models.UserArgument;
import com.redeceleste.celestehomes.models.impls.UserBuilder;
import com.redeceleste.celestehomes.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryManager {
    private static List<Integer> slot = new ArrayList<>();

    public static void homeinventory(Player p, Player t) {
        Inventory inv = Bukkit.createInventory(null, 54, ConfigManager.TitleGUI.replace("%player%", p.getName()));
        InventoryEvent.p = p;

        for (InventoryArgument ai : ConfigManager.Itens) {
            Collections.replaceAll(ai.getLore(), "%player%", p.getName());
            Collections.replaceAll(ai.getLore(), "%amounthomes%", String.valueOf(PermissionManager.getAmountHomes(p)));
            Collections.replaceAll(ai.getLore(), "%maxhomes%", PermissionManager.getPermission(p));

            ItemStack item = new ItemBuilder(ai.getMaterial(), ai.getAmount()).setData(ai.getData()).setName(ai.getName().replace("%player%", p.getName()).replace("%amounthomes%", String.valueOf(PermissionManager.getAmountHomes(p))).replace("%maxhomes%", PermissionManager.getPermission(p))).setLore(ai.getLore()).setGlow(ai.getGlow()).toItemStack();

            for (String enchant : ai.getEnchantament()) {
                String[] split = enchant.split(":");
                item.addUnsafeEnchantment(Enchantment.getByName(ItemBuilder.serializeEnchant(split[0])), Integer.parseInt(split[1]));
            }
            slot.add(ai.getSlot());
            inv.setItem(ai.getSlot(), item);
        }

        try {
            UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
            for (InventoryArgument ai : ConfigManager.Template) {
                for (UserBuilder userBuilder : user.getHomes().values()) {
                    Collections.replaceAll(ai.getLore(), "%number%", String.valueOf(userBuilder.getNumber()));
                    Collections.replaceAll(ai.getLore(), "%home%", ai.getName());

                    ItemStack item = new ItemBuilder(ai.getMaterial()).setData(ai.getData()).setName(ai.getName().replace("%number%", String.valueOf(userBuilder.getNumber())).replace("%home%", userBuilder.getHome())).setLore(ai.getLore()).setGlow(ai.getGlow()).toItemStack();

                    for (String enchant : ai.getEnchantament()) {
                        String[] split = enchant.split(":");
                        item.addUnsafeEnchantment(Enchantment.getByName(ItemBuilder.serializeEnchant(split[0])), Integer.parseInt(split[1]));
                    }
                    inv.setItem(slotEmpty(inv, userBuilder.getNumber()), item);
                }
            }
        } catch (Exception ignore) {
        }
        t.openInventory(inv);
    }

    private static Integer slotEmpty(Inventory inv, Integer slot) {
        for (int i=9;54>=i;i++) {
            if (inv.getItem(slot+i-1) == null) {
                return slot+i-1;
            }
        }
    return null;
    }
}
