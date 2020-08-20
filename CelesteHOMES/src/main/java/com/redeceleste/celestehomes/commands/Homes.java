package com.redeceleste.celestehomes.commands;

import com.redeceleste.celestehomes.managers.ConfigManager;
import com.redeceleste.celestehomes.managers.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Homes implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        
        if (cmd.getName().equalsIgnoreCase("homes")) {
            switch (args.length) {
                case 0:
                    InventoryManager.homeinventory(p, Bukkit.getPlayer(args[0]));
                    return false;
                default:
                    p.sendMessage(ConfigManager.HomeInvalidArgument);
            }
        }
        return false;
    }
}
