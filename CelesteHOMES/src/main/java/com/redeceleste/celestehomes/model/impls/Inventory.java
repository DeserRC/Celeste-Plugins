package com.redeceleste.celestehomes.model.impls;

import com.redeceleste.celestehomes.model.InventoryArgument;
import org.bukkit.Material;

import java.util.List;

public class Inventory extends InventoryArgument {
    public Inventory(String name, Material material, Integer data, Integer slot, Integer amount, Boolean glow, List<String> lore, List<String> en) {
        super(name, material, data, slot, amount, glow, lore, en);
    }
}
