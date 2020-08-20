package com.redeceleste.celestehomes.commands;

import com.redeceleste.celestehomes.managers.HomeManager;
import com.redeceleste.celestehomes.managers.ConfigManager;
import com.redeceleste.celestehomes.managers.InventoryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("home")) {
            switch (args.length) {
                case 0:
                    InventoryManager.homeinventory(p, p);
                    return false;
                case 1:
                    if (HomeManager.isHome(p, args[0])) {
                        HomeManager.homeTeleport(p, args[0]);
                    } else {
                        p.sendMessage(ConfigManager.HomeNotFound);
                    }
                    return false;
                default:
                    p.sendMessage(ConfigManager.HomeInvalidArgument);
            }
        }
        return false;
    }
}
