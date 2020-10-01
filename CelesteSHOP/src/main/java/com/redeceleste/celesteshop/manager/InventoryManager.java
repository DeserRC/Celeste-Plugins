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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
        String title = config.get("Inventory.Title", ConfigType.config);
        String point = points.getPoints(p.getName()).toString();
        Integer size = config.get("Inventory.Size", ConfigType.config);

        title = title.replace("%player%", p.getName());
        title = title.replace("%points%", point);

        Inventory inv = Bukkit.createInventory(new InventoryHolder(InventoryType.menu), size, title);

        setItens(inv, p, point, "Inventory.ItensDecoration.", ConfigType.config);

        for (FileConfiguration file : config.getCategory().getAll()) {
            setTemplate(inv, p, point,file.getName() + ":Categories.Category-Template.", ConfigType.category);
        }

        p.openInventory(inv);
    }

    public void openInventoryCategory(Player p, String file) {
        String title = config.get("Inventory.Title", ConfigType.config);
        String point = points.getPoints(p.getName()).toString();
        Integer size = config.get("Inventory.Size", ConfigType.config);

        title = title.replace("%player%", p.getName());
        title = title.replace("%points%", point);

        Inventory inv = Bukkit.createInventory(new InventoryHolder(InventoryType.category, file), size, title);

        setItens(inv, p, point, file + ":Categories.ItensDecoration", ConfigType.category);
        setItens(inv, p, point, file + ":Categories.ItensBuy", ConfigType.category);
        setTemplate(inv, p, point, file + ":Back", ConfigType.category);

        p.openInventory(inv);
    }

    public void openInventoryConfirm(Player p, String file, String item) {
        String path = file + ":Categories.ItensBuy." + item;
        String title = config.get("InventoryConfirm.Title", ConfigType.config);
        Integer point = points.getPoints(p.getName());
        Integer size = config.get("InventoryConfirm.Size", ConfigType.config);
        ItemStack is = getItem(p, point.toString(), file, ConfigType.category);

        title = title.replace("%player%", p.getName());
        title = title.replace("%points%", point.toString());

        Integer value = config.get(path + ".Price", ConfigType.category);

        if (point < value) {
            if (config.get("Message.PointsInsufficientPoints.Use")) {
                chat.send(p, "Message.PointsInsufficientPoints.Message");
            }
            titleUtil.send(p, "Message.PointsInsufficientPoints.title");
            bar.send(p, "Message.PointsInsufficientPoints.PointsInsufficientPointsActionBar");

            p.closeInventory();
        }

        Inventory inv = Bukkit.createInventory(new InventoryHolder(InventoryType.confirm, file, is), size, title);
        setTemplate(inv, p, point.toString(), "InventoryConfirm.Confirm", ConfigType.config);
        setTemplate(inv, p, point.toString(), "InventoryConfirm.Reject", ConfigType.config);
        setItens(inv, p, point.toString(), "InventoryConfirm.ItensDecoration", ConfigType.config);
        if (config.get(path + ".Use"))
        inv.setItem(config.get(path + ".Slot", ConfigType.category), is);

        p.openInventory(inv);
    }

    public void setItens(Inventory inv, Player p, String point, String path, ConfigType type) {
        for (String menu : config.getKeys(path, ConfigType.config)) {
            Integer slot =      config.get(path + "." + menu + ".Slot", type);
            Integer amount =    config.get(path + "." + menu + ".Amount", type);
            Material material = config.get(path + "." + menu + ".Material", type);
            Integer data =      config.get(path + "." + menu + ".Data", type);
            String name =       config.get(path + "." + menu + ".Name", type);
            Boolean glow =      config.get(path + "." + menu + ".Glow", type);
            List<String> lore = config.getList(path + "." + menu + ".Lore", type);
            List<String> en =   config.getList(path + "." + menu + ".Enchantment", type);

            name = name.replace("%player%", p.getName());
            name = name.replace("%points%", point);

            lore.stream().map(r -> r.replace("%player%", p.getName()).replace("%points%", point));

            inv.setItem(slot, new ItemBuilder(material, amount).setData(data).setName(name).setGlow(glow).setLore(lore).addEnchants(en).toItemStack());
        }
    }

    public void setTemplate(Inventory inv, Player p, String point, String path, ConfigType type) {
        Integer slot =      config.get(path + ".Slot", type);
        Integer amount =    config.get(path + ".Amount", type);
        Material material = config.get(path + ".Material", type);
        Integer data =      config.get(path + ".Data", type);
        String name =       config.get(path + ".Name", type);
        Boolean glow =      config.get(path + ".Glow", type);
        List<String> lore = config.getList(path + ".Lore", type);
        List<String> en =   config.getList(path + ".Enchantment", type);

        name = name.replace("%player%", p.getName());
        name = name.replace("%points%", point);

        lore.stream().map(r -> r.replace("%player%", p.getName()).replace("%points%", point));

        inv.setItem(slot, new ItemBuilder(material, amount).setData(data).setName(name).setGlow(glow).setLore(lore).addEnchants(en).toItemStack());
    }

    public ItemStack getItem(Player p, String point, String path, ConfigType type) {
        Integer amount =    config.get(path + ".Amount", type);
        Material material = config.get(path + ".Material", type);
        Integer data =      config.get(path + ".Data", type);
        String name =       config.get(path + ".Name", type);
        Boolean glow =      config.get(path + ".Glow", type);
        List<String> lore = config.getList(path + ".Lore", type);
        List<String> en =   config.getList(path + ".Enchantment", type);

        name = name.replace("%player%", p.getName());
        name = name.replace("%points%", point);

        lore.stream().map(r -> r.replace("%player%", p.getName()).replace("%points%", point));

        return new ItemBuilder(material, amount).setData(data).setName(name).setGlow(glow).setLore(lore).addEnchants(en).toItemStack();
    }
}