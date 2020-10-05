package com.redeceleste.celesteshop.manager;

import com.redeceleste.celesteshop.CelesteSHOP;
import com.redeceleste.celesteshop.model.ConfigType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {
    private final CelesteSHOP main;
    private final FileConfiguration message;
    private final MapManager<String, FileConfiguration> categories;

    public ConfigManager(CelesteSHOP main) {
        this.main = main;
        this.message = new YamlConfiguration();
        this.categories = new MapManager<>();
        load();
    }

    public <T> T getConfig(String path) {
        return get(path, ConfigType.config);
    }

    public <T> T getMessage(String path) {
        return get(path, ConfigType.message);
    }

    public <T> T getCategory(String path) {
        return get(path, ConfigType.category);
    }

    public <T> T get(String path, ConfigType type) {
        T result = null;

        if (type.equals(ConfigType.config)) {
            result = (T) main.getConfig().get(path, ChatColor.DARK_RED + "There was an error loading the message: " + ChatColor.YELLOW + path);
        }

        if (type.equals(ConfigType.message)) {
            result = (T) message.get(path, ChatColor.DARK_RED + "There was an error loading the message: " + ChatColor.YELLOW + path);
        }

        if (type.equals(ConfigType.category)) {
            String[] file = path.split(":");
            FileConfiguration config = categories.get(file[0]);
            result = (T) config.get(file[1], ChatColor.DARK_RED + "There was an error loading the message: " + ChatColor.YELLOW + path);
        }

        if (result instanceof String) {
            return (T) result.toString().replace('&', '\u00A7');
        }

        return result;
    }

    public List<String> getList(String path, ConfigType type) {
        List<String> list = new ArrayList<>();

        if (type.equals(ConfigType.config)) {
            list = main.getConfig().getStringList(path);
        }

        if (type.equals(ConfigType.message)) {
            list = message.getStringList(path);
        }

        if (type.equals(ConfigType.category)) {
            String[] file = path.split(":");
            FileConfiguration config = categories.get(file[0]);
            list = config.getStringList(file[1]);
        }

        return list.stream().map(r -> r.replace('&', '\u00A7')).collect(Collectors.toList());
    }

    public Set<String> getKeys(String path, ConfigType type) {
        if (type.equals(ConfigType.config)) {
            return main.getConfig().getConfigurationSection(path).getKeys(false);
        }

        if (type.equals(ConfigType.message)) {
            return message.getConfigurationSection(path).getKeys(false);
        }

        String[] customConfig = path.split(":");
        FileConfiguration config = categories.get(customConfig[0]);
        return config.getConfigurationSection(customConfig[1]).getKeys(false);
    }

    public Boolean contains(String path, ConfigType type) {
        if (type.equals(ConfigType.config)) {
            return main.getConfig().contains(path);
        }

        if (type.equals(ConfigType.message)) {
            return message.contains(path);
        }

        String[] customConfig = path.split(":");
        FileConfiguration config = categories.get(customConfig[0]);
        return config.contains(customConfig[1]);
    }

    @SneakyThrows
    private void load() {
        //Config.yml
        main.saveDefaultConfig();

        //Message.yml
        File fileMessage = new File(main.getDataFolder(), "message.yml");
        if (!fileMessage.exists()) {
            main.saveResource("message.yml", false);
        }
        message.load(fileMessage);

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
                categories.put(file.getName(), fileConfiguration);
            }
        }
    }

    @SneakyThrows
    public <T> T reload(String path) {
        main.reloadConfig();
        categories.clear();
        load();

        return getMessage(path);
    }
}