package com.redeceleste.celesteessentials.manager;

import com.redeceleste.celesteessentials.CelesteEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class PowerToolManager {
    private final CelesteEssentials main;
    private final Map<Player, Map<Material, String>> pt;

    public PowerToolManager(CelesteEssentials main) {
        this.main = main;
        this.pt = new HashMap<>();
    }

    public void addPT(Player p, Material material, String command) {
        if (pt.containsKey(p)) {
            Map<Material, String> items = pt.get(p);
            items.put(material, command);
            return;
        }

        Map<Material, String> items = new HashMap<>();
        items.put(material, command);
        pt.put(p, items);
    }

    public void removePT(Player p, Material material) {
        if (!pt.containsKey(p)) return;
        Map<Material, String> items = pt.get(p);
        items.remove(material);
    }

    public void clearPlayer(Player p) {
        pt.remove(p);
    }

    public void sendCommand(Player p, Material material) {
        String command = pt.get(p).get(material);
        Bukkit.dispatchCommand(p, command);
    }

    public Boolean contains(Player p, Material material) {
        if (!pt.containsKey(p)) return false;
        return pt.get(p).containsKey(material);
    }
}
