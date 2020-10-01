package com.redeceleste.celesteshop.manager;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.model.ConfigType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {
    private final Main main;
    private final FileConfiguration message;
    private final MapManager<String, FileConfiguration> category;

    public ConfigManager(Main main) {
        this.main = main;
        this.message = new YamlConfiguration();
        this.category = new MapManager<>();
        load();
    }

    public <T> T get(String path) {
        return get(path, ConfigType.message);
    }

    public <T> T get(String path, ConfigType type) {
        if (type.equals(ConfigType.message)) {
            T result = (T) message.get(path, ChatColor.DARK_RED + "There was an error loading the message: " + ChatColor.YELLOW + path);

            if (result instanceof String) {
                return (T) result.toString().replace('&', '\u00A7');
            }

            return result;
        }

        if (type.equals(ConfigType.config)) {
            T result = (T) main.getConfig().get(path, ChatColor.DARK_RED + "There was an error loading the message: " + ChatColor.YELLOW + path);

            if (result instanceof String) {
                return (T) result.toString().replace('&', '\u00A7');
            }

            return result;
        }

        String[] customConfig = path.split(":");
        FileConfiguration config = category.get(customConfig[0]);
        T result = (T) config.get(customConfig[1], ChatColor.DARK_RED + "There was an error loading the message: " + ChatColor.YELLOW + path);

        if (result instanceof String) {
            return (T) result.toString().replace('&', '\u00A7');
        }

        return result;
    }

    public List<String> getList(String path, ConfigType type) {
        if (type.equals(ConfigType.message)) {
            List<String> list = message.getStringList(path);
            list = list.stream().map(r -> r.replace('&', '\u00A7')).collect(Collectors.toList());
            return list;
        }

        if (type.equals(ConfigType.config)) {
            List<String> list = main.getConfig().getStringList(path);
            list = list.stream().map(r -> r.replace('&', '\u00A7')).collect(Collectors.toList());
            return list;
        }

        String[] customConfig = path.split(":");
        FileConfiguration config = category.get(customConfig[0]);

        List<String> list = config.getStringList(customConfig[1]);
        list = list.stream().map(r -> r.replace('&', '\u00A7')).collect(Collectors.toList());
        return list;
    }

    public Set<String> getKeys(String path, ConfigType type) {
        if (type.equals(ConfigType.message)) {
            return message.getConfigurationSection(path).getKeys(false);
        }

        if (type.equals(ConfigType.config)) {
            return main.getConfig().getConfigurationSection(path).getKeys(false);
        }

        String[] customConfig = path.split(":");
        FileConfiguration config = category.get(customConfig[0]);
        return config.getConfigurationSection(customConfig[1]).getKeys(false);
    }

    @SneakyThrows
    private void load() {
        //Config.yml
        main.saveDefaultConfig();

        //Message.yml
        File fileMessage = new File(main.getDataFolder(), "message.yml");
        if (fileMessage.exists()) {
            main.saveResource("message.yml", false);
            message.load(fileMessage);
        }

        //Category-example.yml
        File fileCategory = new File(main.getDataFolder() + "/categories");
        if (!fileCategory.exists()) {
            fileCategory.mkdirs();
            main.saveResource("categories/category-example.yml", false);
        }

        //Custom Categories
        File[] listCategory = fileCategory.listFiles(File::isFile);
        FileConfiguration fileConfiguration = new YamlConfiguration();

        for (File file : listCategory) {
            if (!file.getName().equals("category-example.yml")) {
                fileConfiguration.load(file);
                category.put(file.getName(), fileConfiguration);
            }
        }
    }

    @SneakyThrows
    public <T> T reload(String path) {
        //Config.yml
        main.reloadConfig();

        //Message.yml
        File fileMessage = new File(main.getDataFolder(), "message.yml");
        message.load(fileMessage);

        //Custom Categories
        File fileCategory = new File(main.getDataFolder() + "/categories");
        File[] listCategory = fileCategory.listFiles(File::isFile);
        FileConfiguration fileConfiguration = new YamlConfiguration();
        category.clear();

        for (File file : listCategory) {
            if (!file.getName().equals("category-example.yml")) {
                fileConfiguration.load(file);
                category.put(file.getName(), fileConfiguration);
            }
        }

        return get(path);
    }
}