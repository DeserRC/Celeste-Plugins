package com.redeceleste.celestekits.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

@AllArgsConstructor
@Getter
public abstract class CategoryArgument {
    private final String name;
    private final Inventory inv;
    private final FileConfiguration file;
}
