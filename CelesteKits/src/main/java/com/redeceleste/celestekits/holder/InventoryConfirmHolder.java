package com.redeceleste.celestekits.holder;

import com.redeceleste.celestekits.model.CategoryArgument;
import com.redeceleste.celestekits.model.KitArgument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@AllArgsConstructor
@Getter
public class InventoryConfirmHolder implements InventoryHolder {
    private final CategoryArgument category;
    private final KitArgument kit;

    @Override
    public Inventory getInventory() {
        return null;
    }
}
