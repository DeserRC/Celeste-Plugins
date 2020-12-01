package com.redeceleste.celestekits.holder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@AllArgsConstructor
@Getter
public class InventoryCategoryHolder implements InventoryHolder {
    private final int slot;

    @Override
    public Inventory getInventory() {
        return null;
    }
}
