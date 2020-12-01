package com.redeceleste.celestekits.manager;

import com.redeceleste.celestekits.MockKits;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {
    private final MockKits main;
    private final FileConfiguration config;
    private final FileConfiguration message;
    private final Map<File, FileConfiguration> categories;
    private final Map<FileConfiguration, Map<File, FileConfiguration>> kits;

    public ConfigManager(MockKits main) {
        this.main = main;
        this.config = new YamlConfiguration();
        this.message = new YamlConfiguration();
        this.categories = new HashMap<>();
        this.kits = new HashMap<>();
        load();
    }

    public <T> T getConfig(String path) {
        return get(path, config);
    }

    public <T> T getMessage(String path) {
        return get(path, message);
    }

    public <T> T get(String path, FileConfiguration file) {
        T result = (T) file.get(path, ChatColor.DARK_RED + "Ocorreu um Error ao carregar a mensagem: " + ChatColor.YELLOW + path);
        if (result instanceof String) {
            return (T) result.toString().replace("&", "\u00A7");
        }
        return result;
    }

    public List<String> getListConfig(String path) {
        return getList(path, config);
    }

    public List<String> getListMessage(String path) {
        return getList(path, message);
    }

    public List<String> getList(String path, FileConfiguration file) {
        List<String> list = file.getStringList(path);
        return list.stream().map(r -> r.replace("&", "\u00A7")).collect(Collectors.toList());
    }

    public Set<String> getKeys(String path, FileConfiguration file) {
        return file.getConfigurationSection(path).getKeys(false);
    }

    public Boolean contains(String path, FileConfiguration file) {
        return file.contains(path);
    }

    @SneakyThrows
    public void load() {
        // Config.yml
        File fileConfig = new File(main.getDataFolder(), "config.yml");
        if (!fileConfig.exists()) {
            main.saveResource("config.yml", false);
        }
        config.load(fileConfig);

        // Message.yml
        File fileMessage = new File(main.getDataFolder(), "message.yml");
        if (!fileMessage.exists()) {
            main.saveResource("message.yml", false);
        }
        message.load(fileMessage);

        // category-example.yml
        File fileCategories = new File(main.getDataFolder() + "/categories");
        if (!fileCategories.exists()) {
            fileCategories.mkdirs();
            main.saveResource("categories/category-example.yml", false);
        }

        // kit-example.yml
        File fileKits = new File(main.getDataFolder() + "/kits");
        if (!fileKits.exists()) {
            fileKits.mkdirs();
            main.saveResource("kits/kit-example.yml", false);
        }

        // Categories and kits
        File[] listCategories = fileCategories.listFiles(File::isFile);
        File[] listKits = fileKits.listFiles(File::isFile);
        categories.clear();
        kits.clear();

        for (File category : listCategories) {
            if (category.getName().equals("category-example.yml")) continue;
            String categoryName = category.getName().replace(".yml", "");
            categoryName = categoryName.replace(".yml", "");

            FileConfiguration categoryFile = new YamlConfiguration();
            categoryFile.load(category);

            Map<File, FileConfiguration> categoryKits = new HashMap<>();
            for (File kit : listKits) {
                if (kit.getName().equals("kit-example.yml")) continue;
                FileConfiguration kitFile = new YamlConfiguration();
                kitFile.load(kit);

                String itemCategory = get("Category", kitFile);
                if (!itemCategory.equalsIgnoreCase(categoryName)) continue;
                categoryKits.put(kit, kitFile);
            }

            kits.put(categoryFile, categoryKits);
            categories.put(category, categoryFile);
        }
    }
}