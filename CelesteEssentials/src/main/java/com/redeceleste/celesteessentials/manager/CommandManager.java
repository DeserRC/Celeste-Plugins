package com.redeceleste.celesteessentials.manager;

import com.redeceleste.celesteessentials.CelesteEssentials;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandManager {
    private final CelesteEssentials main;
    private final ConfigManager config;

    public CommandManager(CelesteEssentials main) {
        this.main = main;
        this.config = main.getConfigManager();
    }

    public void send(Player p, String command) {
        List<String> message = getMessage(command);
        if (message == null) return;
        message.forEach(p::sendMessage);
    }

    public boolean commandExist(String command) {
        return getMessage(command) != null;
    }

    private List<String> getMessage(String command) {
        for (String name : config.getKeys("Commands", config.getConfig())) {
            String path = "Commands." + name + ".";
            String cmd = config.getConfig(path + "Command");
            if (!cmd.equalsIgnoreCase(command.toLowerCase())) continue;
            return config.getListConfig(path + "Message");
        }
        return null;
    }
}
