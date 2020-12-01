package com.redeceleste.celesteessentials.manager;

import com.redeceleste.celesteessentials.CelesteEssentials;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Locale.US;

@Getter
public class ConfigManager {
    private final CelesteEssentials main;
    private final FileConfiguration config;
    private final FileConfiguration message;
    private final FileConfiguration warp;
    private final DecimalFormat format;
    private final List<String> prefix;
    private File fileWarp;

    public ConfigManager(CelesteEssentials main) {
        this.main = main;
        this.config = new YamlConfiguration();
        this.message = new YamlConfiguration();
        this.warp = new YamlConfiguration();
        this.format = new DecimalFormat("#.###", new DecimalFormatSymbols(US));
        this.prefix = asList("", "K", "M", "B", "T", "Q", "QUI", "SEX", "SEP", "OCT", "NON", "DEC", "UND");
        load();
    }

    public <T> T getConfig(String path) {
        return get(path, config);
    }

    public <T> T getMessage(String path) {
        return get(path, message);
    }

    public <T> T getWarp(String path) {
        return get(path, warp);
    }

    public <T> T get(String path, FileConfiguration config) {
        T result = (T) config.get(path, ChatColor.DARK_RED + "Ocorreu um erro ao carregar a mensagem: " + ChatColor.YELLOW + path);
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

    public List<String> getListWarp(String path) {
        return getList(path, warp);
    }

    public List<String> getList(String path, FileConfiguration config) {
        List<String> list = config.getStringList(path);
        return list.stream().map(r -> r.replace("&", "\u00A7")).collect(Collectors.toList());
    }

    public Set<String> getKeys(String path, FileConfiguration config) {
        return config.getConfigurationSection(path).getKeys(false);
    }

    public Boolean contains(String path, FileConfiguration config) {
        return config.contains(path);
    }

    public <T extends Number> String formatTps(T value) {
        if ((Double) value < 16) return "§c" + format.format(value);
        if ((Double) value < 18) return "§6" + format.format(value);
        if ((Double) value < 19) return "§e" + format.format(value);
        if ((Double) value <= 20) return "§a" + format.format(value);
        if ((Double) value > 20) return "§a20*";
        return format.format(value);
    }

    @SneakyThrows
    private void load() {
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

        // Warps.yml
        fileWarp = new File(main.getDataFolder(), "warps.yml");
        if (!fileWarp.exists()) {
            main.saveResource("warps.yml", false);
        }
        warp.load(fileWarp);
    }

    public void reload() {
        load();
        reloadWarp();
    }

    @SneakyThrows
    public void reloadWarp() {
        warp.save(fileWarp);
        fileWarp = new File(main.getDataFolder(), "warps.yml");
        if (!fileWarp.exists()) {
            main.saveResource("warps.yml", false);
        }
        warp.load(fileWarp);
    }
}