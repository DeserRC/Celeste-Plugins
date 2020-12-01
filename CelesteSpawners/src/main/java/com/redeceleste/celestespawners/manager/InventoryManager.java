package com.redeceleste.celestespawners.manager;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.ItemBuilder;
import com.redeceleste.celestespawners.holder.InventoryConfirmHolder;
import com.redeceleste.celestespawners.holder.InventoryShopHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.bukkit.Material.SKULL_ITEM;
import static org.bukkit.Material.getMaterial;

public class InventoryManager {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private int maximumPage;

    public InventoryManager(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
        setMaximumPage();
    }

    public void open(Player p, int page) {
        double money = main.getEconomy().getBalance(p);
        String moneyFormat = config.formatPrefix(money);

        String title = config.getInventory("Inventory.Title");
        int size = config.getInventory("Inventory.Size");

        title = title
                .replace("{player}", p.getName())
                .replace("{money}", moneyFormat);

        Inventory inv = Bukkit.createInventory(new InventoryShopHolder(page), size, title);

        setDecoration(inv,"Inventory.Decoration", null,
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{page}", String.valueOf(page)));
        setTemplate(p, inv, page);
        setPageChange(inv, page,
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{page}", page));

        p.openInventory(inv);
    }

    public void confirm(Player p, FileConfiguration itemFile, int page) {
        p.closeInventory();
        String name = config.get("Name", itemFile);
        int amount = config.get("Amount", itemFile);

        double money = config.get("Price.Money", itemFile);
        String moneyFormat = config.formatPrefix(money);

        String title = config.getInventory("Inventory-Confirm.Title");
        int size = config.getInventory("Inventory-Confirm.Size");

        title = title
                .replace("{player}", p.getName())
                .replace("{money}", moneyFormat)
                .replace("{name}", name);

        ItemStack item = getItem(itemFile, "",
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{name}", name),
                build("{amount}", amount),
                build("{page}", page));

        Inventory inv = Bukkit.createInventory(new InventoryConfirmHolder(itemFile, page, item), size, title);

        setDecoration(inv,"Inventory-Confirm.Decoration", item,
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{name}", name),
                build("{amount}", amount),
                build("{page}", page));
        setConfirmation(inv,
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{name}", name),
                build("{amount}", amount),
                build("{page}", page));

        p.openInventory(inv);
    }

    public void nextPage(Player p, Inventory inv, int page) {
        replaceItems(p, inv,page + 1);
    }

    public void previousPage(Player p, Inventory inv, int page) {
        replaceItems(p, inv,page - 1);
    }

    public void replaceItems(Player p, Inventory inv, int page) {
        ((InventoryShopHolder) inv.getHolder()).setPage(page);

        double money = main.getEconomy().getBalance(p);
        String moneyFormat = config.formatPrefix(money);

        inv.clear();
        setDecoration(inv,"Inventory.Decoration", null,
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{page}", String.valueOf(page)));
        setTemplate(p, inv, page);
        setPageChange(inv, page,
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{page}", page));
    }

    public void changeAmount(Player p, FileConfiguration itemFile, Inventory inv, int amount, int page) {
        String name       = config.get("Name", itemFile);
        int initialAmount = config.get("Amount", itemFile);
        double money      = config.get("Price.Money", itemFile);

        String finalAmount = String.valueOf(initialAmount * amount);
        String moneyFormat = config.formatPrefix(money);

        ItemStack item = getItem(itemFile, "",
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{name}", name),
                build("{amount}", finalAmount),
                build("{page}", page));

        ((InventoryConfirmHolder) inv.getHolder()).setItem(item);

        setDecoration(inv,"Inventory-Confirm.Decoration", item,
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{name}", name),
                build("{amount}", finalAmount),
                build("{page}", page));
        setConfirmation(inv,
                build("{player}", p.getName()),
                build("{money}", moneyFormat),
                build("{name}", name),
                build("{amount}", finalAmount),
                build("{page}", page));
    }

    @SafeVarargs
    private final void setDecoration(Inventory inv, String path, ItemStack itemStack, Map.Entry<String, String>... map) {
        FileConfiguration file = config.getInventory();
        for (String name : config.getKeys(path, file)) {
            String newPath = path + "." + name + ".";
            int slot = config.get(newPath + "Slot", file);

            ItemStack item;
            boolean contains = config.contains(newPath + "Template", file);
            if (contains) {
                boolean use = config.get(newPath + "Template.Use", file);
                if (use) item = itemStack;
                else item = getItem(file, newPath, map);
            } else item = getItem(file, newPath, map);
            inv.setItem(slot, item);
        }
    }

    private void setTemplate(Player p, Inventory inv, int page) {
        for (FileConfiguration file : config.getItems().values()) {
            int itemPage = config.get("Page", file);
            if (itemPage != page) continue;

            String name = config.get("Name", file);
            int amount = config.get("Amount", file);
            int slot = config.get("Slot", file);
            double money = config.get("Price.Money", file);

            String moneyFormat = config.formatPrefix(money);
            ItemStack item = getItem(file, "",
                    build("{player}", p.getName()),
                    build("{money}", moneyFormat),
                    build("{name}", name),
                    build("{amount}", amount),
                    build("{page}", page));

            List<String> loreShowCase = config.getList("Lore-Showcase", file);
            loreShowCase = loreShowCase.stream().map(r -> r
                    .replace("{player}", p.getName())
                    .replace("{money}", moneyFormat)
                    .replace("{name}", name)
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{page}", String.valueOf(page)))
                    .collect(toList());

            item = new ItemBuilder(item)
                    .addLore(loreShowCase)
                    .toItemStack();
            inv.setItem(slot, item);
        }
    }

    @SafeVarargs
    private final void setPageChange(Inventory inv, int page, Map.Entry<String, String>... map) {
        FileConfiguration file = config.getInventory();
        if (maximumPage != page) {
            String path = "Inventory.Custom-Items.Next-Page.";
            int slot = config.get(path + "Slot", file);
            ItemStack item = getItem(file, path, map);
            inv.setItem(slot, item);
        }

        if (page != 1) {
            String path = "Inventory.Custom-Items.Previous-Page.";
            int slot = config.get(path + "Slot", file);
            ItemStack item = getItem(file, path, map);
            inv.setItem(slot, item);
        }
    }

    @SafeVarargs
    private final void setConfirmation(Inventory inv, Map.Entry<String, String>... map) {
        FileConfiguration file = config.getInventory();
        String path = "Inventory-Confirm.Custom-Items.Confirm.";
        int slot = config.get(path + "Slot", file);
        ItemStack item = getItem(file, path, map);
        inv.setItem(slot, item);

        path = "Inventory-Confirm.Custom-Items.Deny.";
        slot = config.get(path + "Slot", file);
        item = getItem(file, path, map);
        inv.setItem(slot, item);

        path = "Inventory-Confirm.Custom-Items.Add.";
        slot = config.get(path + "Slot", file);
        item = getItem(file, path, map);
        inv.setItem(slot, item);

        path = "Inventory-Confirm.Custom-Items.Remove.";
        slot = config.get(path + "Slot", file);
        item = getItem(file, path, map);
        inv.setItem(slot, item);
    }

    @SafeVarargs
    public final ItemStack getItem(FileConfiguration file, String path, Map.Entry<String, String>... map) {
        int amount        = config.get(path + "Amount", file);
        Material material = getMaterial(config.get(path + "Material", file));
        int data          = config.get(path + "Data", file);
        String name       = config.get(path + "Name", file);
        boolean glow      = config.get(path + "Glow", file);
        List<String> lore = config.getList(path + "Lore", file);
        List<String> en   = config.getList(path + "Enchantment", file);

        for (Map.Entry<String, String> entry : map) {
            name = name.replace(entry.getKey(), entry.getValue());
            lore = lore.stream().map(line -> line
                    .replace(entry.getKey(), entry.getValue()))
                    .collect(toList());
        }

        boolean isHead = config.get(path + "Head.Use", file);
        if (isHead) {
            String texture = config.get(path + "Head.Texture", file);
            UUID uuid;

            boolean contains = config.contains(path + "Head.UUID", file);
            if (contains) uuid = UUID.fromString(config.get(path + "Head.UUID", file));
            else uuid =          UUID.randomUUID();

            return new ItemBuilder(SKULL_ITEM, amount)
                    .setData(3)
                    .setName(name)
                    .setGlow(glow)
                    .setLore(lore)
                    .addEnchant(en)
                    .setSkull(texture, uuid).toItemStack();
        }
        return new ItemBuilder(material, amount)
                .setData(data)
                .setName(name)
                .setGlow(glow)
                .setLore(lore)
                .addEnchant(en).toItemStack();
    }

    public <T, U> Map.Entry<String, String> build(T key, U value) {
        return new AbstractMap.SimpleEntry<>(key.toString(), value.toString());
    }

    private void setMaximumPage() {
        maximumPage = 1;
        for (FileConfiguration file : config.getItems().values()) {
            int page = config.get("Page", file);
            if (page <= maximumPage) continue;
            maximumPage = page;
        }
    }
}