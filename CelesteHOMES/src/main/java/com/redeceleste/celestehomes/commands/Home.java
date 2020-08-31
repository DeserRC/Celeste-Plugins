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
            if (args.length == 0) {
                InventoryManager.homeinventory(p, p);
                return false;
            }

            if (args.length != 1) {
                p.sendMessage(ConfigManager.HomeInvalidArgument);
                return false;
            }

            if (!HomeManager.isHome(p, args[0])) {
                p.sendMessage(ConfigManager.HomeNotFound);
                return false;
            }

            HomeManager.homeTeleport(p, args[0]);
        }
        return false;
    }
}
