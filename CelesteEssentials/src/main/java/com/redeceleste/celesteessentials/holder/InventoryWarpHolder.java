package com.redeceleste.celesteessentials.holder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@AllArgsConstructor
@Getter
public class InventoryWarpHolder implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }
}
