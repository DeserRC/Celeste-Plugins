package com.redeceleste.celestespawners.manager;

import com.redeceleste.celestespawners.CelesteSpawners;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.round;
import static java.util.Arrays.asList;
import static java.util.Locale.*;
import static java.util.stream.Collectors.toList;

@Getter
public class ConfigManager {
    private final CelesteSpawners main;
    private final Map<String, FileConfiguration> customMobs;
    private final Map<String, FileConfiguration> items;
    private final FileConfiguration config;
    private final FileConfiguration message;
    private final FileConfiguration inventory;
    private final List<String> prefixes;
    private DecimalFormat format;
    private File logSpawner, logPurchase;

    public ConfigManager(CelesteSpawners main) {
        this.main = main;
        this.customMobs = new HashMap<>();
        this.items = new HashMap<>();
        this.config = new YamlConfiguration();
        this.message = new YamlConfiguration();
        this.inventory = new YamlConfiguration();
        this.prefixes = asList("", "K", "M", "B", "T", "Q", "QUI", "SEX", "SEP", "OCT", "NON", "DEC", "UND");
        load();
    }

    public <T> T getConfig(String path) {
        return get(path, config);
    }

    public <T> T getMessage(String path) {
        return get(path, message);
    }

    public <T> T getInventory(String path) {
        return get(path, inventory);
    }

    public <T> T get(String path, FileConfiguration config) {
        T result = (T) config.get(path, ChatColor.DARK_RED + "Ocorreu um erro ao carregar a mensagem: " + ChatColor.YELLOW + path);
        if (result instanceof String) {
            return (T) result.toString().replace('&', '\u00A7');
        }
        return result;
    }

    public List<String> getListConfig(String path) {
        return getList(path, config);
    }

    public List<String> getListMessage(String path) {
        return getList(path, message);
    }

    public List<String> getListInventory(String path) {
        return getList(path, inventory);
    }

    public List<String> getList(String path, FileConfiguration config) {
        List<String> list = config.getStringList(path);
        return list.stream().map(r -> r
                .replace('&', '\u00A7'))
                .collect(toList());
    }

    public Set<String> getKeys(String path, FileConfiguration config) {
        return config.getConfigurationSection(path).getKeys(false);
    }

    public boolean contains(String path, FileConfiguration config) {
        return config.contains(path);
    }

    public void putLogSpawner(String message) throws IOException {
        boolean useSpawner = getConfig("Logs.Use-Spawner");
        if (!useSpawner) return;

        String timeZone = get("Logs.Time-Zone", config);
        SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss] ");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logSpawner, true))) {
            bw.write(sdf.format(new Date()) + message);
            bw.newLine();
        }
    }

    public void putLogPurchase(String message) throws IOException {
        boolean usePurchase = getConfig("Logs.Use-Purchase");
        if (!usePurchase) return;

        String timeZone = get("Logs.Time-Zone", config);
        SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss] ");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPurchase, true))) {
            bw.write(sdf.format(new Date()) + message);
            bw.newLine();
        }
    }

    public <T extends Number> String formatPrefix(T value) {
        StringBuilder sb = new StringBuilder();
        double dValue = Double.parseDouble(value.toString());
        int i=0;

        for (;dValue/1000 >= 1; dValue/=1000, i++);
        double round = round((dValue) * 100.0) / 100.0;
        sb.append(round);
        sb.append(prefixes.get(i));

        if (i == 0) return format.format(value);
        return sb.toString();
    }


    public <T extends Number> String formatDecimal(T value) {
        return format.format(value);
    }

    @SneakyThrows
    private void load() {
        // config.yml
        File fileConfig = new File(main.getDataFolder(), "config.yml");
        if (!fileConfig.exists()) {
            main.saveResource("config.yml", false);
        }
        config.load(fileConfig);

        // message.yml
        File fileMessage = new File(main.getDataFolder(), "message.yml");
        if (!fileMessage.exists()) {
            main.saveResource("message.yml", false);
        }
        message.load(fileMessage);

        // inventory.yml
        File fileInventory = new File(main.getDataFolder(), "inventory.yml");
        if (!fileInventory.exists()) {
            main.saveResource("inventory.yml", false);
        }
        inventory.load(fileInventory);


        // item-example.yml
        File fileItems = new File(main.getDataFolder() + "/items");
        if (!fileItems.exists()) {
            fileItems.mkdirs();
            main.saveResource("items/item-example.yml", false);
        }

        // items
        File[] listItems = fileItems.listFiles(File::isFile);
        items.clear();
        for (File file : listItems) {
            if (!file.getName().equals("item-example.yml")) {
                FileConfiguration fileConfiguration = new YamlConfiguration();
                fileConfiguration.load(file);
                items.put(file.getName().replace(".yml", "").toUpperCase(), fileConfiguration);
            }
        }

        // mob-example.yml
        File fileMobs = new File(main.getDataFolder() + "/mobs");
        if (!fileMobs.exists()) {
            fileMobs.mkdirs();
            main.saveResource("mobs/mob-example.yml", false);
        }

        // mobs
        File[] listMobs = fileMobs.listFiles(File::isFile);
        customMobs.clear();
        for (File file : listMobs) {
            if (!file.getName().equals("mob-example.yml")) {
                FileConfiguration fileConfiguration = new YamlConfiguration();
                fileConfiguration.load(file);
                customMobs.put(file.getName().replace(".yml", "").toUpperCase(), fileConfiguration);
            }
        }

        // decimal format
        boolean useUS = getConfig("Spawner.Use-US-Format");
        if (useUS) format = new DecimalFormat("#,###", new DecimalFormatSymbols(US));
        else format = new DecimalFormat("#,###", new DecimalFormatSymbols(GERMAN));

        // logs
        String timeZone = get("Logs.Time-Zone", config);
        SimpleDateFormat sdf = new SimpleDateFormat(get("Logs.Format", config));
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

        boolean useSpawner = getConfig("Logs.Use-Spawner");
        if (useSpawner) {
            logSpawner = new File(main.getDataFolder() + "/logs/spawners", sdf.format(new Date()) + ".log");
            if (!logSpawner.getParentFile().exists()) logSpawner.getParentFile().mkdirs();
            if (!logSpawner.exists()) logSpawner.createNewFile();
        }

        boolean usePurchase = getConfig("Logs.Use-Purchase");
        if (usePurchase) {
            logPurchase = new File(main.getDataFolder() + "/logs/purchase", sdf.format(new Date()) + ".log");
            if (!logPurchase.getParentFile().exists()) logPurchase.getParentFile().mkdirs();
            if (!logPurchase.exists()) logPurchase.createNewFile();
        }
    }
}