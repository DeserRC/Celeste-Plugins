package com.redeceleste.celestekits.manager;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.builder.ItemBuilder;
import com.redeceleste.celestekits.holder.InventoryConfirmHolder;
import com.redeceleste.celestekits.holder.InventoryViewerHolder;
import com.redeceleste.celestekits.model.CategoryArgument;
import com.redeceleste.celestekits.model.KitArgument;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.redeceleste.celestekits.util.DateUtil.formatDate;
import static java.util.stream.Collectors.toList;
import static org.bukkit.Bukkit.*;
import static org.bukkit.Material.SKULL_ITEM;
import static org.bukkit.Material.getMaterial;

public class InventoryManager {
    private final CelesteKit main;
    private final ConfigManager config;
    private final KitManager kit;
    private final UserManager user;

    public InventoryManager(CelesteKit main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.kit = main.getKitFactory().getKit();
        this.user = main.getUserFactory().getUser();
    }

    public void openMain(Player p) {
        p.openInventory(kit.getMainInv());
    }

    public void openCategory(Player p, CategoryArgument categoryArg) {
        Inventory inv = categoryArg.getInv();
        Inventory newInv = createInventory(inv.getHolder(), inv.getSize(), inv.getTitle());
        newInv.setContents(inv.getContents());

        for (KitArgument kitArg : kit.getKits(categoryArg)) {
            FileConfiguration kitFile = kitArg.getFile();
            String name = config.get("Name", kitFile);
            String stats = getStats(p, kitArg, kitFile);
            String delay = formatDate(System.currentTimeMillis() + kitArg.getDelay() + 1000);

            setTemplate(newInv, kitFile,
                    build("{player}", p.getName()),
                    build("{name}", name),
                    build("{delay}", delay),
                    build("{stats}", stats));
        }
        p.openInventory(newInv);
    }

    public void openViewer(Player p, CategoryArgument categoryArg, KitArgument kitArg) {
        String title = config.getConfig("Inventory-View.Title");
        int size = config.getConfig("Inventory-View.Size");

        Inventory inv = createInventory(new InventoryViewerHolder(categoryArg), size, title);

        FileConfiguration kitFile = kitArg.getFile();
        String name = config.get("Name", kitFile);
        String stats = getStats(p, kitArg, kitFile);
        String delay = formatDate(System.currentTimeMillis() + kitArg.getDelay() + 1000);

        ItemStack itemStack = getItem(kitFile, "",
                build("{player}", p.getName()),
                build("{name}", name),
                build("{delay}", delay),
                build("{stats}", stats));

        setDecoration(inv, main.getConfig(), "Inventory-View.Decoration", itemStack,
                build("{player}", p.getName()),
                build("{name}", name),
                build("{delay}", delay),
                build("{stats}", stats));
        setCustomItems(inv, main.getConfig(), "Inventory-View.Custom-Items",
                build("{player}", p.getName()),
                build("{name}", name));
        setItems(inv, kitFile, "Items",
                build("{player}", p.getName()),
                build("{name}", name),
                build("{delay}", delay),
                build("{stats}", stats));
        p.openInventory(inv);
    }

    public void openConfirm(Player p, CategoryArgument categoryArg, KitArgument kitArg) {
        String title = config.getConfig("Inventory-View.Title");
        int size = config.getConfig("Inventory-View.Size");

        Inventory inv = createInventory(new InventoryConfirmHolder(categoryArg, kitArg), size, title);

        FileConfiguration kitFile = kitArg.getFile();
        String name = config.get("Name", kitFile);
        String stats = getStats(p, kitArg, kitFile);
        String delay = formatDate(System.currentTimeMillis() + kitArg.getDelay() + 1000);

        ItemStack itemStack = getItem(kitFile, "",
                build("{player}", p.getName()),
                build("{name}", name),
                build("{delay}", delay),
                build("{stats}", stats));

        setDecoration(inv, main.getConfig(), "Inventory-Confirm.Decoration", itemStack,
                build("{player}", p.getName()),
                build("{name}", name),
                build("{delay}", delay),
                build("{stats}", stats));
        setCustomItems(inv, main.getConfig(), "Inventory-Confirm.Custom-Items",
                build("{player}", p.getName()),
                build("{name}", name),
                build("{delay}", delay),
                build("{stats}", stats));
        p.openInventory(inv);
    }

    @SafeVarargs
    public final void setTemplate(Inventory inv, FileConfiguration kitFile, Map.Entry<String, String>... map) {
        ItemStack item = getItem(kitFile, "", map);
        int slot = config.get("Slot", kitFile);
        inv.setItem(slot, item);
    }

    @SafeVarargs
    public final void setCustomItems(Inventory inv, FileConfiguration file, String path, Map.Entry<String, String>... map) {
        for (String name : config.getKeys(path, file)) {
            String newPath = path + "." + name + ".";
            int slot = config.get(newPath + "Slot", file);
            ItemStack item = getItem(file, newPath, map);
            inv.setItem(slot, item);
        }
    }

    @SafeVarargs
    public final void setDecoration(Inventory inv, FileConfiguration file, String path, ItemStack itemStack, Map.Entry<String, String>... map) {
        for (String name : config.getKeys(path, file)) {
            String newPath = path + "." + name + ".";
            int slot = config.get(newPath + "Slot", file);

            ItemStack item;
            boolean contains = config.contains(newPath + "Template", file);
            if (contains) {
                boolean use   = config.get(newPath + "Template.Use", file);
                if (use) item = itemStack;
                else item     = getItem(file, newPath, map);
            } else item       = getItem(file, newPath, map);
            inv.setItem(slot, item);
        }
    }

    @SafeVarargs
    private final void setItems(Inventory inv, FileConfiguration file, String path, Map.Entry<String, String>... map) {
        int slot = 0;
        for (String name : config.getKeys(path, file)) {
            String newPath = path + "." + name + ".";
            while (inv.getItem(slot) != null) {
                slot++;
            }

            ItemStack item = getItem(file, newPath, map);
            inv.setItem(slot, item);
        }
    }

    @SafeVarargs
    public final ItemStack getItem(FileConfiguration file, String path, Map.Entry<String, String>... map) {
        int amount =        config.get(path + "Amount", file);
        Material material = getMaterial(config.get(path + "Material", file));
        int data =          config.get(path + "Data", file);
        String name =       config.get(path + "Name", file);
        boolean glow =      config.get(path + "Glow", file);
        List<String> lore = config.getList(path + "Lore", file);
        List<String> en =   config.getList(path + "Enchantment", file);

        for (Map.Entry<String, String> entry : map) {
            name = name.replace(entry.getKey(), entry.getValue());
            lore = lore.stream().map(line -> line.replace(entry.getKey(), entry.getValue())).collect(toList());
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
                .addEnchant(en)
                .toItemStack();
    }

    public void sendItemPlayer(Player p, String name, Map<String, Map<ItemStack, List<String>>> items) {
        for (String prefix : items.keySet()) {
            for (ItemStack itemStack : items.get(prefix).keySet()) {
                List<String> commands = items.get(prefix).get(itemStack);
                if (commands.size() != 0) {
                    String amount = String.valueOf(itemStack.getAmount());
                    commands = commands.stream().map(cmd -> cmd
                            .replace("{player}", p.getName())
                            .replace("{name}", name)
                            .replace("{amount}", amount))
                            .collect(toList());

                    commands.forEach(cmd -> dispatchCommand(getConsoleSender(), cmd));
                    continue;
                }

                String itemName = null;
                if (itemStack.getItemMeta().hasDisplayName()) {
                    itemName = itemStack.getItemMeta().getDisplayName()
                            .replace("{player}", p.getName())
                            .replace("{name}", name);
                }

                List<String> itemLore = null;
                if (itemStack.getItemMeta().hasLore()) {
                    itemLore = itemStack.getItemMeta().getLore();
                    itemLore = itemLore.stream().map(r -> r
                            .replace("{player}", p.getName())
                            .replace("{name}", name))
                            .collect(toList());
                }

                p.getInventory().addItem(new ItemBuilder(itemStack)
                        .setName(itemName)
                        .setLore(itemLore)
                        .toItemStack());
            }
        }
    }

    private String getStats(Player p, KitArgument kitArg, FileConfiguration kitFile) {
        String name = config.get("Name", kitFile);
        String kitName = kitArg.getName();

        boolean containsCD = false;
        if (user.getCooldown(p.getName()) != null) {
            containsCD = user.getCooldown(p.getName()).getKit().containsKey(kitName);
        }

        String adminPermission = config.getConfig("Permission.Admin");
        String kitPermission = kitArg.getPermission();

        if (containsCD && !p.hasPermission(adminPermission)) {
            long delay = user.getCooldown(p.getName()).getKit().get(kitName);
            String delayFormatted = formatDate(delay);

            String stats = config.getMessage("Pattern.No-Available");
            return stats
                    .replace("{name}", name)
                    .replace("{delay}", delayFormatted);
        }

        if (kitPermission != null && !p.hasPermission(adminPermission) && !p.hasPermission(kitPermission)) {
            String stats = config.getMessage("Pattern.No-Permission");
            return stats.replace("{name}", name);
        }

        String stats = config.getMessage("Pattern.Available");
        return stats.replace("{name}", name);
    }

    public <T, U> Map.Entry<String, String> build(T key, U value) {
        return new AbstractMap.SimpleEntry<>(key.toString(), value.toString());
    }
}
