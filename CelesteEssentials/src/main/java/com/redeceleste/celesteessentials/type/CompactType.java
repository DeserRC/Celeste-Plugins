package com.redeceleste.celesteessentials.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.*;

@AllArgsConstructor
@Getter
public enum CompactType {
    COAL(COAL_BLOCK),
    IRON_INGOT(IRON_BLOCK),
    GOLD_INGOT(GOLD_BLOCK),
    DIAMOND(DIAMOND_BLOCK),
    EMERALD(EMERALD_BLOCK),
    INK_SACK(LAPIS_BLOCK),
    GOLD_NUGGET(Material.GOLD_INGOT),
    MELON(MELON_BLOCK),
    REDSTONE(REDSTONE_BLOCK),
    SLIME_BALL(SLIME_BLOCK);

    private final Material material;

    public static CompactType getType(ItemStack item) {
        String name = item.getType().name();
        for (CompactType type : values()) {
            if (type.name().equalsIgnoreCase(name)) return type;
        }
        return null;
    }

    public static int compact(Player p, PlayerInventory inv, ItemStack... items) {
        int amountCompacted = 0;
        List<ItemStack> totalRemainder = new ArrayList<>();

        for (ItemStack item : items) {
            if (item == null) continue;
            if (item.getAmount() < 9 || item.hasItemMeta()) continue;
            if (item.getType().equals(Material.INK_SACK) && item.getDurability() != 4) continue;

            CompactType type = getType(item);
            if (type == null) continue;

            int initialAmount = item.getAmount();
            int amount = initialAmount / 9;
            int remainder = initialAmount - (amount * 9);

            ItemStack newItemStack = new ItemStack(type.getMaterial(), amount);
            inv.remove(item);
            inv.addItem(newItemStack);

            if (remainder > 0) {
                ItemStack remainderItemStack = new ItemStack(item.getType(), remainder, item.getDurability());
                totalRemainder.add(remainderItemStack);
            }
            amountCompacted += amount * 9;
        }

        for (ItemStack item : totalRemainder) {
            for (ItemStack remainder : inv.addItem(item).values()) {
                p.getWorld().dropItem(p.getLocation(), remainder);
            }
        }
        return amountCompacted;
    }
}
