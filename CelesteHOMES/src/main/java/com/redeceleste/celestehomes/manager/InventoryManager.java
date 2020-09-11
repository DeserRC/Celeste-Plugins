package com.redeceleste.celestehomes.manager;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.builder.InventoryBuilder;
import com.redeceleste.celestehomes.model.InventoryArgument;
import com.redeceleste.celestehomes.model.UserArgument;
import com.redeceleste.celestehomes.builder.UserBuilder;
import com.redeceleste.celestehomes.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryManager {
    private static List<Integer> slot = new ArrayList<>();

    public static void homeinventory(Player p, Player t) {
        Inventory inv = Bukkit.createInventory(new InventoryBuilder(p), 54, ConfigManager.TitleGUI.replace("%player%", p.getName()));
        String amountHomes = PermissionManager.getAmountHomes(p).toString();
        String maxhomes = PermissionManager.getPermission(p);
        String remainingHomes = PermissionManager.remainingHomes(p, Integer.valueOf(maxhomes)).toString();

        for (InventoryArgument ai : ConfigManager.Itens) {
            List<String> replacedLore = ai.getLore().stream().map(s -> s
                    .replace("%player%", p.getName())
                    .replace("%amounthomes%", amountHomes)
                    .replace("%maxhomes%", maxhomes)
                    .replace("%remaininghomes%", remainingHomes)).collect(Collectors.toList());

            ItemStack item = new ItemBuilder(ai.getMaterial(), ai.getAmount()).setData(ai.getData()).setName(ai.getName()
                    .replace("%player%", p.getName())
                    .replace("%amounthomes%", PermissionManager.getAmountHomes(p).toString())
                    .replace("%maxhomes%", PermissionManager.getPermission(p))
                    .replace("%remaininghomes%", remainingHomes)).setLore(replacedLore).setGlow(ai.getGlow()).toItemStack();

            for (String en : ai.getEnchantament()) {
                String[] split = en.split(":");
                item.addUnsafeEnchantment(ItemBuilder.serializeEnchant(split[0]), Integer.parseInt(split[1]));
            }
            slot.add(ai.getSlot());
            inv.setItem(ai.getSlot(), item);
        }

        try {
            UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
            for (InventoryArgument ai : ConfigManager.Template) {
                for (UserBuilder userBuilder : user.getHomes().values()) {
                    List<String> replacedLore = ai.getLore().stream().map(s -> s
                            .replace("%number%", userBuilder.getNumber().toString())
                            .replace("%home%", userBuilder.getName())).collect(Collectors.toList());

                    ItemStack item = new ItemBuilder(ai.getMaterial()).setData(ai.getData()).setName(ai.getName()
                            .replace("%number%", userBuilder.getNumber().toString())
                            .replace("%home%", userBuilder.getName())).setLore(replacedLore).setGlow(ai.getGlow()).toItemStack();

                    for (String enchant : ai.getEnchantament()) {
                        String[] split = enchant.split(":");
                        item.addUnsafeEnchantment(ItemBuilder.serializeEnchant(split[0]), Integer.parseInt(split[1]));
                    }
                    inv.setItem(slotEmpty(inv, userBuilder.getNumber()), item);
                }
            }
        } catch (Exception ignore) { }
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
