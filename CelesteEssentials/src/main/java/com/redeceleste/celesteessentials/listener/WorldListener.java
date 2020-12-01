package com.redeceleste.celesteessentials.listener;

import com.redeceleste.celesteessentials.CelesteEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {
    private final CelesteEssentials main;

    public WorldListener(CelesteEssentials main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        if (e.toWeatherState()) e.setCancelled(true);
    }
}
