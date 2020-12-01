package com.redeceleste.celesteessentials.util;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GeoIPUtil {
    @SneakyThrows
    public static String getCountry(Player p) {
        URL url = new URL("http://ip-api.com/json/" + p.getPlayer().getAddress().getHostName());
        try (BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder sb = new StringBuilder();
            String inputLine;

            while ((inputLine = stream.readLine()) != null) {
                sb.append(inputLine);
            }

            if (!(sb.toString().contains("\"country\":\""))) return "Not found";
            return sb.toString().split("\"country\":\"") [1].split("\",") [0];
        }
    }
}
