package com.redeceleste.celesteessentials.manager;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.builder.ItemBuilder;
import com.redeceleste.celesteessentials.holder.InventoryWarpHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.Material.SKULL_ITEM;
import static org.bukkit.Material.getMaterial;

public class InventoryManager {
    private final CelesteEssentials main;
    private final ConfigManager config;

    public InventoryManager(CelesteEssentials main) {
        this.main = main;
        this.config = main.getConfigManager();
    }

    public void openWarp(Player p) {
        String title = config.getConfig("Inventory-Warp.Title");
        Integer size = config.getConfig("Inventory-Warp.Size");

        title = title.replace("{player}", p.getName());

        Inventory inv = Bukkit.createInventory(new InventoryWarpHolder(), size, title);

        setDecoration(p, inv, "Inventory-Warp.Decoration.");
        setTemplate(p, inv);

        p.openInventory(inv);
    }


    private void setDecoration(Player p, Inventory inv, String path) {
        FileConfiguration file = config.getConfig();
        for (String name : config.getKeys(path, file)) {
            String newPath = path + "." + name + ".";
            int slot = config.get(newPath + "Slot", file);
            ItemStack item = getItem(p, file, newPath);
            inv.setItem(slot, item);
        }
    }

    private void setTemplate(Player p, Inventory inv) {
        FileConfiguration file = config.getWarp();
        for (String name : config.getKeys("", config.getWarp())) {
            String newPath = name + ".Inventory" + ".";
            int slot = config.get(newPath + "Slot", file);
            ItemStack item = getItem(p, file, newPath);
            inv.setItem(slot, item);
        }
    }

    public ItemStack getItem(Player p, FileConfiguration file, String path) {
        int amount        = config.get(path + "Amount", file);
        Material material = getMaterial(config.get(path + "Material", file));
        int data          = config.get(path + "Data", file);
        String name       = config.get(path + "Name", file);
        boolean glow      = config.get(path + "Glow", file);
        List<String> lore = config.getList(path + "Lore", file);
        List<String> en   = config.getList(path + "Enchantment", file);

        name = name.replace("{player}", p.getName());
        lore = lore.stream().map(r -> r
                .replace("{player}", p.getName()))
                .collect(Collectors.toList());

        boolean isHead = config.get(path + "Head.Use", file);
        if (isHead) {
            String texture = config.get(path + "Head.Texture", file);
            return new ItemBuilder(SKULL_ITEM, amount)
                    .setData(3)
                    .setName(name)
                    .setGlow(glow)
                    .setLore(lore)
                    .addEnchant(en)
                    .setSkull(texture, UUID.randomUUID()).toItemStack();
        }
        return new ItemBuilder(material, amount)
                .setData(data)
                .setName(name)
                .setGlow(glow)
                .setLore(lore)
                .addEnchant(en).toItemStack();
    }
}