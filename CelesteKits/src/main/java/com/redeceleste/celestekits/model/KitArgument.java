package com.redeceleste.celestekits.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public abstract class KitArgument {
    private final String name;
    private final String permission;
    private final long delay;
    private final FileConfiguration file;
    private Map<String, Map<ItemStack, List<String>>> items;
}
