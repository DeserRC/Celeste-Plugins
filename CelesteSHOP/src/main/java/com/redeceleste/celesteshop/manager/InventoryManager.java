package com.redeceleste.celesteshop.manager;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.builder.ItemBuilder;
import com.redeceleste.celesteshop.holder.InventoryHolder;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.model.InventoryType;
import com.redeceleste.celesteshop.util.impl.BarUtil;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import com.redeceleste.celesteshop.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryManager {
    private final Main main;
    private final ConfigManager config;
    private final PointsManager points;
    private final ChatUtil chat;
    private final TitleUtil titleUtil;
    private final BarUtil bar;

    public InventoryManager(Main main) {
         this.main = main;
         this.config = main.getConfigManager();
         this.points = main.getPointsFactory().getManager();
         this.chat = main.getMessageFactory().getChat();
         this.titleUtil = main.getMessageFactory().getTitle();
         this.bar = main.getMessageFactory().getBar();
    }

    public void openInventory(Player p) {
        String title = config.getConfig("Inventory.Title");
        String point = points.getPoints(p.getName()).toString();
        Integer size = config.getConfig("Inventory.Size");

        title = title.replace("%player%", p.getName());
        title = title.replace("%points%", point);

        Inventory inv = Bukkit.createInventory(new InventoryHolder(InventoryType.menu), size, title);

        setItemsConfig(inv, p, point, "Inventory.ItemsDecoration");
        config.getCategories().getKeys().forEach(file -> setTemplateCategory(inv, p, point, file + ":Categories.Category-Template"));

        p.openInventory(inv);
    }

    public void openInventoryCategory(Player p, String file) {
        String title = config.getCategory(file + ":Categories.Title");
        String point = points.getPoints(p.getName()).toString();
        Integer size = config.getCategory(file + ":Categories.Size");

        title = title.replace("%player%", p.getName());
        title = title.replace("%points%", point);

        Inventory inv = Bukkit.createInventory(new InventoryHolder(InventoryType.category, file), size, title);

        setItemsCategory(inv, p, point, file + ":Categories.ItemsDecoration");
        setItemsCategory(inv, p, point, file + ":Categories.ItemsBuy");
        setTemplateCategory(inv, p, point, file + ":Categories.Back");

        p.openInventory(inv);
    }

    public void openInventoryConfirm(Player p, String path) {
        String title = config.getConfig("InventoryConfirm.Title");
        Integer point = points.getPoints(p.getName());
        Integer size = config.getConfig("InventoryConfirm.Size");
        Integer value = config.getCategory(path + ".Price");
        ItemStack is = getItem(p, point.toString(), path, ConfigType.category);

        title = title.replace("%player%", p.getName());
        title = title.replace("%points%", point.toString());

        if (point < value) {
            chat.send(p, "Message.PointsInsufficientPoints");
            titleUtil.send(p, "Message.PointsInsufficientPointsTitle");
            bar.send(p, "Message.PointsInsufficientPointsActionBar");

            p.closeInventory();
            return;
        }

        Inventory inv = Bukkit.createInventory(new InventoryHolder(InventoryType.confirm, path, is), size, title);
        setItemsConfig(inv, p, point.toString(), "InventoryConfirm.ItemsDecoration");
        setTemplateConfig(inv, p, point.toString(), "InventoryConfirm.Confirm");
        setTemplateConfig(inv, p, point.toString(), "InventoryConfirm.Reject");

        if (config.getConfig("InventoryConfirm.ItemTemplate.Use")) {
            inv.setItem(config.getConfig("InventoryConfirm.ItemTemplate.Slot"), is);
        }

        p.openInventory(inv);
    }

    private void setItemsConfig(Inventory inv, Player p, String point, String path) {
        for (String menu : config.getKeys(path, ConfigType.config)) {
            Integer slot = config.get(path + "." + menu + ".Slot", ConfigType.config);
            inv.setItem(slot, getItem(p, point, path + "." + menu, ConfigType.config));
        }
    }

    private void setItemsCategory(Inventory inv, Player p, String point, String path) {
        for (String menu : config.getKeys(path, ConfigType.category)) {
            Integer slot = config.get(path + "." + menu + ".Slot", ConfigType.category);
            inv.setItem(slot, getItem(p, point, path + "." + menu, ConfigType.category));
        }
    }

    private void setTemplateConfig(Inventory inv, Player p, String point, String path) {
        Integer slot = config.get(path + ".Slot", ConfigType.config);
        inv.setItem(slot, getItem(p, point, path, ConfigType.config));
    }

    private void setTemplateCategory(Inventory inv, Player p, String point, String path) {
        Integer slot = config.get(path + ".Slot", ConfigType.category);
        inv.setItem(slot, getItem(p, point, path, ConfigType.category));
    }

    private ItemStack getItem(Player p, String point, String path, ConfigType type) {
        Integer amount =    config.get(path + ".Amount", type);
        Material material = Material.getMaterial(config.get(path + ".Material", type));
        Integer data =      config.get(path + ".Data", type);
        String name =       config.get(path + ".Name", type);
        Boolean glow =      config.get(path + ".Glow", type);
        List<String> lore = config.getList(path + ".Lore", type);
        List<String> en =   config.getList(path + ".Enchantment", type);

        name = name.replace("%player%", p.getName());
        name = name.replace("%points%", point);

        lore = lore.stream().map(r -> r.replace("%player%", p.getName()).replace("%points%", point)).collect(Collectors.toList());

        return new ItemBuilder(material, amount).setData(data).setName(name).setGlow(glow).setLore(lore).addEnchants(en).toItemStack();
    }
}