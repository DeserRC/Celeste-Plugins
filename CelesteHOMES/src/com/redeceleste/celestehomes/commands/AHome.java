package com.redeceleste.celestehomes.commands;

import com.redeceleste.celestehomes.managers.ConfigManager;
import com.redeceleste.celestehomes.managers.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AHome implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("ahome")) {
            switch (args.length) {
                case 1:
                    InventoryManager.homeinventory(Bukkit.getPlayer(args[0]), p);
                    return false;
                default:
                    p.sendMessage(ConfigManager.HomeInvalidArgument);
            }
        }
        return false;
    }
}
