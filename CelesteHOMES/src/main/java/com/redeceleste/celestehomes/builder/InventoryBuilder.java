package com.redeceleste.celestehomes.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@AllArgsConstructor
@Getter
public class InventoryBuilder implements InventoryHolder {
    private Player player;

    @Override
    public Inventory getInventory() {
        return null;
    }
}
