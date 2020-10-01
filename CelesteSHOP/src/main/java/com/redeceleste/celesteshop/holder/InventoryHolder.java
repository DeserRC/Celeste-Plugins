package com.redeceleste.celesteshop.holder;

import com.redeceleste.celesteshop.model.InventoryType;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class InventoryHolder implements org.bukkit.inventory.InventoryHolder {
    private final InventoryType type;
    private String file;
    private ItemStack is;

    public InventoryHolder(InventoryType type, String file, ItemStack is) {
        this.type = type;
        this.file = file;
        this.is = is;
    }

    public InventoryHolder(InventoryType type, String file) {
        this.type = type;
        this.file = file;
    }

    public InventoryHolder(InventoryType type) {
        this.type = type;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
