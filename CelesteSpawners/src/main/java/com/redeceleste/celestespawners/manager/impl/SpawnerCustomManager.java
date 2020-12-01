package com.redeceleste.celestespawners.manager.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.ItemBuilder;
import com.redeceleste.celestespawners.builder.SpawnerBuilder;
import com.redeceleste.celestespawners.factory.SpawnerFactory;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.HologramManager;
import com.redeceleste.celestespawners.manager.Manager;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.type.ArmorType;
import com.redeceleste.celestespawners.type.MobType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.redeceleste.celestespawners.type.ArmorType.*;
import static org.bukkit.Material.SKULL_ITEM;

public class SpawnerCustomManager extends Manager {
    private final CelesteSpawners main;
    private final SpawnerFactory spawn;
    private final ConfigManager config;
    private final HologramManager hologram;

    public SpawnerCustomManager(CelesteSpawners main, SpawnerFactory spawn) {
        super(main, spawn);
        this.main = main;
        this.spawn = spawn;
        this.config = main.getConfigManager();
        this.hologram = main.getHologramManager();
    }

    public void createSpawner(String location, String fileName) {
        createSpawner(location, fileName, 1L);
    }

    public void createSpawner(String location, String fileName, long amount) {
        if (!isExist(fileName)) return;
        FileConfiguration spawnerFile = getSpawnerFile(fileName);
        String name = config.get("Name", spawnerFile);

        String typeName = config.get("Mob", spawnerFile);
        MobType type = MobType.getMob(typeName);

        Map<ArmorType, ItemStack> armors = getArmor(spawnerFile);
        ItemStack head = getHead(spawnerFile);

        Hologram spawnerHologram = hologram.createHologram(location, name, amount, head);
        spawn.getSpawners().put(location, new SpawnerBuilder()
                .location(location)
                .hologram(spawnerHologram)
                .type(type)
                .amount(amount)
                .name(name)
                .head(head)
                .armors(armors)
                .buildCustom());
    }

    public SpawnerArgument getArgument(String location, String mobName, long amount) {
        String fileName = getFileName(mobName);
        if (fileName == null) return null;

        FileConfiguration spawnerFile = getSpawnerFile(fileName);
        String name = config.get("Name", spawnerFile);

        String typeName = config.get("Mob", spawnerFile);
        MobType type = MobType.getMob(typeName);

        Map<ArmorType, ItemStack> armors = getArmor(spawnerFile);
        ItemStack head = getHead(spawnerFile);

        Hologram spawnerHologram = hologram.createHologram(location, name, amount, head);
        return new SpawnerBuilder()
                .location(location)
                .hologram(spawnerHologram)
                .type(type)
                .amount(amount)
                .name(name)
                .head(head)
                .armors(armors)
                .buildCustom();
    }

    public ItemStack getSpawner(String fileName, long amount, MobType type) {
        if (!isExist(fileName)) return null;
        String customName = getName(fileName);
        String nameFormat = config.getConfig("Spawner.Custom-Spawner.Name");

        List<String> loreFormat = config.getListConfig("Spawner.Custom-Spawner.Lore");
        ItemStack spawner = getSpawner(customName, String.valueOf(amount), nameFormat, loreFormat, type, true);
        return new ItemBuilder(spawner).setGlow(true).toItemStack();
    }

    public Map<ItemStack, Integer> getDrops(String fileName) {
        fileName = getFileName(fileName);

        FileConfiguration spawnerFile = getSpawnerFile(fileName);
        Map<ItemStack, Integer> drops = new HashMap<>();
        for (String drop : config.getKeys("Drops", spawnerFile)) {
            String path = "Drops." + drop;

            int chance =    config.get(path + ".Chance", spawnerFile);
            boolean useHead =   config.get(path + ".Head.Use", spawnerFile);

            String name = config.get(path + ".Name", spawnerFile);
            boolean glow = config.get(path + ".Glow", spawnerFile);

            List<String> lore = config.getList(path + ".Lore", spawnerFile);
            List<String> en = config.getList(path + ".Enchantment", spawnerFile);

            if (useHead) {
                String uuid = config.get(path + ".Head.UUID", spawnerFile);
                String texture = config.get(path + ".Head.Texture", spawnerFile);
                ItemStack item = new ItemBuilder(SKULL_ITEM, 1)
                        .setData(3)
                        .setName(name)
                        .setLore(lore)
                        .addEnchant(en)
                        .setSkull(texture, UUID.fromString(uuid)).toItemStack();
                drops.put(item, chance);
                continue;
            }

            Material material = Material.valueOf(config.get(path + ".Material", spawnerFile));
            int data = config.get(path + ".Data", spawnerFile);

            ItemStack item = new ItemBuilder(material, 1)
                    .setData(data)
                    .setName(name)
                    .setGlow(glow)
                    .setLore(lore)
                    .addEnchant(en).toItemStack();
            drops.put(item, chance);
        }
        return drops;
    }

    public MobType getMob(String fileName) {
        if (!isExist(fileName)) return null;
        FileConfiguration file = getSpawnerFile(fileName);
        String mobName = config.get("Mob", file);
        return MobType.getMob(mobName);
    }

    public boolean isExist(String fileName) {
        return config.getCustomMobs().containsKey(fileName.toUpperCase());
    }

    public String getFileName(ItemStack item) {
        String name = getName(item);
        for (String fileName : config.getCustomMobs().keySet()) {
            FileConfiguration file = config.getCustomMobs().get(fileName);
            String customName = config.get("Name", file);
            if (customName.equalsIgnoreCase(name)) return fileName;
        }
        return null;
    }

    public String getFileName(String mobName) {
        for (String fileName : config.getCustomMobs().keySet()) {
            FileConfiguration spawnerFile = config.getCustomMobs().get(fileName);
            String customName = config.get("Name", spawnerFile);
            if (customName.equalsIgnoreCase(mobName)) return fileName;
            if (fileName.equalsIgnoreCase(mobName)) return fileName;
        }
        return null;
    }

    private FileConfiguration getSpawnerFile(String fileName) {
        return config.getCustomMobs().get(fileName.toUpperCase());
    }

    private String getName(String fileName) {
        if (!isExist(fileName)) return null;
        FileConfiguration file = getSpawnerFile(fileName);
        return config.get("Name", file);
    }

    private ItemStack getHead(FileConfiguration spawnerFile) {
        String texture = config.get("Head", spawnerFile);
        if (texture != null) return new ItemBuilder(SKULL_ITEM, 1, 3)
                .setSkull(texture, UUID.randomUUID())
                .toItemStack();

        String mobName = config.get("Mob", spawnerFile);
        MobType mob = MobType.getMob(mobName);
        return mob.getHead();
    }

    private Map<ArmorType, ItemStack> getArmor(FileConfiguration spawnerFile) {
        Map<ArmorType, ItemStack> armors = new HashMap<>();
        for (String armorName : config.getKeys("Armor", spawnerFile)) {
            String path = "Armor." + armorName;
            boolean useArmor = config.get(path + ".Use", spawnerFile);

            if (!useArmor) continue;
            List<String> en = config.getList(path + ".Enchantment", spawnerFile);
            ArmorType armor = ArmorType.getArmor(armorName);

            if (armor.equals(HELMET)) {
                boolean useHead = config.get(path + ".Use-Head", spawnerFile);
                if (useHead) {
                    ItemStack head = getHead(spawnerFile);
                    ItemStack item = new ItemBuilder(head)
                            .addEnchant(en)
                            .toItemStack();
                    armors.put(armor, item);
                    continue;
                }
            }

            Material material = Material.valueOf(config.get(path + ".Material", spawnerFile));
            int data = config.get(path + ".Data", spawnerFile);
            boolean glow = config.get(path + ".Glow", spawnerFile);

            ItemStack item = new ItemBuilder(material, 1, data)
                    .setGlow(glow)
                    .addEnchant(en)
                    .toItemStack();
            armors.put(armor, item);
        }
        return armors;
    }
}