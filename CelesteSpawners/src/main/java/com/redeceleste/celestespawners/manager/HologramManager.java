package com.redeceleste.celestespawners.manager;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.redeceleste.celestespawners.util.LocationUtil.deserialize;

public class HologramManager {
    private final CelesteSpawners main;
    private final ConfigManager config;

    public HologramManager(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
    }

    public Hologram createHologram(String location, String name, long amount, ItemStack head) {
        name = name.toUpperCase();

        Location loc = deserialize(location);
        double height = config.getConfig("Stack-Spawner.Hologram.Height");

        Hologram hologram = HologramsAPI.createHologram(main, loc.add(0.5, height,0.5));
        List<String> format = config.getListConfig("Stack-Spawner.Hologram.Format");

        String amountFormatted = config.formatDecimal(amount);
        for (String line : format) {
            if (line.contains("{mobhead}")) {
                hologram.appendItemLine(head);
            } else hologram.appendTextLine(line
                        .replace("{amount}", amountFormatted)
                        .replace("{type}", name));
        }
        return hologram;
    }

    public void updateHologram(SpawnerArgument spawnerArg) {
        long amount = spawnerArg.getAmount();

        Hologram hologram = spawnerArg.getHologram();
        List<String> format = config.getListConfig("Stack-Spawner.Hologram.Format");

        String amountFormatted = config.formatDecimal(amount);
        for (String line : format) {
            if (!line.contains("{amount}")) continue;
            int index = format.indexOf(line);

            hologram.removeLine(index);
            hologram.insertTextLine(index, line
                    .replace("{amount}", amountFormatted));
        }
    }

    public void deleteHologram(SpawnerArgument spawnerArg) {
        spawnerArg.getHologram().delete();
    }
}
