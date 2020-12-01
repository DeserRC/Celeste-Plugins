package com.redeceleste.celestekits.manager;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.factory.KitFactory;
import com.redeceleste.celestekits.holder.InventoryCategoryHolder;
import com.redeceleste.celestekits.holder.InventoryMainHolder;
import com.redeceleste.celestekits.model.CategoryArgument;
import com.redeceleste.celestekits.model.KitArgument;
import com.redeceleste.celestekits.model.impl.Category;
import com.redeceleste.celestekits.model.impl.Kit;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.bukkit.Material.AIR;

public class KitManager {
    private final CelesteKit main;
    private final KitFactory kit;
    private final ConfigManager config;
    private InventoryManager inventory;

    public KitManager(CelesteKit main, KitFactory kit) {
        this.main = main;
        this.kit = kit;
        this.config = main.getConfigManager();
    }

    public void load() {
        inventory = main.getInventoryManager();
        kit.getCategories().clear();
        kit.getKits().clear();

        String title = config.getConfig("Inventory-Categories.Title");
        int size = config.getConfig("Inventory-Categories.Size");

        Inventory mainInv = Bukkit.createInventory(new InventoryMainHolder(), size, title);
        inventory.setDecoration(mainInv, main.getConfig(), "Inventory-Categories.Decoration", null);

        for (File file : config.getCategories().keySet()) {
            FileConfiguration categoryFile = config.getCategories().get(file);
            String fileName = file.getName();
            fileName = fileName.replace(".yml", "");

            // Main menu
            ItemStack itemStack = inventory.getItem(categoryFile, "");
            int categorySlot = config.get("Slot", categoryFile);
            mainInv.setItem(categorySlot, itemStack);

            // Categories Menu
            title = config.get("Title", categoryFile);
            size = config.get("Size", categoryFile);

            Inventory categoryInv = Bukkit.createInventory(new InventoryCategoryHolder(categorySlot), size, title);
            inventory.setDecoration(categoryInv, categoryFile, "Decoration", null);
            inventory.setCustomItems(categoryInv, categoryFile, "Custom-Items");

            Map<Integer, KitArgument> kits = new HashMap<>();
            for (File fileKit : config.getKits().get(categoryFile).keySet()) {
                FileConfiguration kitFile = config.getKits().get(categoryFile).get(fileKit);

                int kitSlot = config.get("Slot", kitFile);
                KitArgument kitArg = loadKits(fileKit, kitFile);
                kits.put(kitSlot, kitArg);
            }

            CategoryArgument categoryArg = new Category(fileName, categoryInv, categoryFile);
            kit.getCategories().put(categorySlot, categoryArg);
            kit.getKits().put(categoryArg, kits);
        }
        kit.setMainInv(mainInv);
    }

    private KitArgument loadKits(File file, FileConfiguration kitFile) {
        String fileName = file.getName();
        fileName = fileName.replace(".yml", "");

        String perm = null;
        String kitPerm = config.get("Permission", kitFile);
        if (!kitPerm.equals("")) perm = kitPerm;

        Map<String, Map<ItemStack, List<String>>> items = new HashMap<>();
        for (String item : config.getKeys("Items", kitFile)) {
            String path = "Items." + item + ".";

            ItemStack itemStack = inventory.getItem(kitFile, path);
            List<String> commands = new ArrayList<>();

            boolean useCommand = config.get(path + ".Command.Use", kitFile);
            if (useCommand) commands = config.getList(path + ".Command.Commands", kitFile);

            Map<ItemStack, List<String>> itemsAndCommand = new HashMap<>();
            itemsAndCommand.put(itemStack, commands);
            items.put(item, itemsAndCommand);
        }

        int delay = config.get("Delay", kitFile);
        return new Kit(fileName, perm, SECONDS.toMillis(delay), kitFile, items);
    }

    @SneakyThrows
    public void setItems(CategoryArgument categoryArg, FileConfiguration kitFile, ItemStack[] items) {
        int itemSlot = 0;
        kitFile.set("Items", null);
        for (ItemStack itemStack : items) {
            if (itemStack == null || itemStack.getType().equals(AIR)) continue;
            ItemMeta meta = itemStack.getItemMeta();
            String itemName = "";
            if (meta.hasDisplayName()) {
                itemName = meta.getDisplayName();
            }

            short data = itemStack.getDurability();

            List<String> enchants = new ArrayList<>();
            Map<Enchantment, Integer> itemEnchant = meta.getEnchants();
            for (Enchantment enchant : itemEnchant.keySet()) {
                String enchantName = enchant.getName();
                int level = itemEnchant.get(enchant);
                enchants.add(enchantName + ":" + level);
            }

            kitFile.set("Items." + itemSlot + ".Amount", itemStack.getAmount());
            kitFile.set("Items." + itemSlot + ".Material", itemStack.getType().name());
            kitFile.set("Items." + itemSlot + ".Data", (int) data);
            kitFile.set("Items." + itemSlot + ".Name", itemName);
            kitFile.set("Items." + itemSlot + ".Glow", false);
            kitFile.set("Items." + itemSlot + ".Lore", meta.getLore());
            kitFile.set("Items." + itemSlot + ".Enchantment", enchants);
            kitFile.set("Items." + itemSlot + ".Head.Use", false);
            kitFile.set("Items." + itemSlot + ".Head.UUID", "");
            kitFile.set("Items." + itemSlot + ".Head.Texture", "");
            kitFile.set("Items." + itemSlot + ".Command.Use", false);
            kitFile.set("Items." + itemSlot + ".Command.Commands", new ArrayList<>());
            itemSlot++;
        }

        Map<File, FileConfiguration> kitsFiles = config.getKits().get(categoryArg.getFile());
        for (File fileKit : config.getKits().get(categoryArg.getFile()).keySet()) {
            FileConfiguration kitConfig = kitsFiles.get(fileKit);
            if (!kitConfig.equals(kitFile)) continue;
            kitFile.save(fileKit);
        }
    }

    @SneakyThrows
    public void create(CategoryArgument categoryArg, String name, String permission, int delay, ItemStack[] items) {
        File file = new File(main.getDataFolder() + "/kits", name.toLowerCase() + ".yml");
        if (file.exists()) file.delete();
        file.createNewFile();

        FileConfiguration categoryFile = categoryArg.getFile();
        FileConfiguration kitFile = new YamlConfiguration();
        kitFile.load(file);

        Map<Integer, KitArgument> kits = kit.getKits().get(categoryArg);

        int slot = 0;
        while (kits.containsKey(slot)) slot++;

        List<String> lore = asList("", "&7Kit " + name.toUpperCase(), "&c&lEXCLUSIVO: &7para Animais", "", "&7Status:", "&6{stats}");
        kitFile.set("Category", categoryArg.getName());
        kitFile.set("Delay", delay);
        kitFile.set("Permission", permission);
        kitFile.set("Slot", slot);
        kitFile.set("Amount", 1);
        kitFile.set("Material", "STONE");
        kitFile.set("Data", 0);
        kitFile.set("Name", "&6&l" + name.toUpperCase());
        kitFile.set("Glow", false);
        kitFile.set("Lore", lore);
        kitFile.set("Enchantment", new ArrayList<>());
        kitFile.set("Head.Use", false);
        kitFile.set("Head.Texture", "");
        kitFile.save(file);
        config.getKits().get(categoryFile).put(file, kitFile);
        setItems(categoryArg, kitFile, items);

        KitArgument kitArg = loadKits(file, kitFile);
        kits.put(slot, kitArg);
    }

    @SneakyThrows
    public void set(CategoryArgument categoryArg, KitArgument kitArg, File fileKit, ItemStack[] items) {
        Map<Integer, KitArgument> kits = kit.getKits().get(categoryArg);

        int slot = 0;
        for (Integer kitSlot : kits.keySet()) {
            if (!kits.get(kitSlot).equals(kitArg)) continue;
            slot = kitSlot;
            break;
        }

        FileConfiguration kitFile = kitArg.getFile();
        setItems(categoryArg, kitFile, items);
        kitArg = loadKits(fileKit, kitFile);
        kits.put(slot, kitArg);
    }

    public Inventory getMainInv() {
        return kit.getMainInv();
    }

    public CategoryArgument getCategory(String name) {
        for (CategoryArgument categoryArg : kit.getCategories().values()) {
            if (!categoryArg.getName().equalsIgnoreCase(name)) continue;
            return categoryArg;
        }
        return null;
    }

    public KitArgument getKit(String name) {
        for (Map<Integer, KitArgument> kits : kit.getKits().values()) {
            for (KitArgument kitArg : kits.values()) {
                if (!kitArg.getName().equalsIgnoreCase(name)) continue;
                return kitArg;
            }
        }
        return null;
    }

    public Collection<KitArgument> getKits(CategoryArgument categoryArg) {
        return kit.getKits().get(categoryArg).values();
    }
}
