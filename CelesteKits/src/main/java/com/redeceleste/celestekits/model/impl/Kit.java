package com.redeceleste.celestekits.model.impl;

import com.redeceleste.celestekits.model.KitArgument;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class Kit extends KitArgument {
    public Kit(String name, String permission, long delay, FileConfiguration file, Map<String, Map<ItemStack, List<String>>> items) {
        super(name, permission, delay, file, items);
    }
}
