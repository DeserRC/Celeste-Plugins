package com.redeceleste.celestespawners.holder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
@Setter
public class InventoryShopHolder implements InventoryHolder {
    private int page;

    public InventoryShopHolder(int page) {
        this.page = page;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
