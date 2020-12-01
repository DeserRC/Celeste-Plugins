package com.redeceleste.celestekits.holder;

import com.redeceleste.celestekits.model.CategoryArgument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@AllArgsConstructor
@Getter
public class InventoryViewerHolder implements InventoryHolder {
    private final CategoryArgument category;

    @Override
    public Inventory getInventory() {
        return null;
    }
}
