package com.redeceleste.celestespawners.holder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class InventoryConfirmHolder implements InventoryHolder {
    private final FileConfiguration itemFile;
    private final int page;
    private ItemStack item;
    private int amount;

    public InventoryConfirmHolder(FileConfiguration itemFile, int page, ItemStack item) {
        this.itemFile = itemFile;
        this.page = page;
        this.item = item;
        this.amount = 1;
    }

    public void addAmount() {
        amount++;
    }

    public void removeAmount() {
        amount--;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
