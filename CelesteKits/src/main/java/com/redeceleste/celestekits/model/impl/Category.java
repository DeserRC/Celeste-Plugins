package com.redeceleste.celestekits.model.impl;

import com.redeceleste.celestekits.model.CategoryArgument;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

public class Category extends CategoryArgument {
    public Category(String name, Inventory inv, FileConfiguration file) {
        super(name, inv, file);
    }
}
