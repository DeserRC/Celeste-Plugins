package com.redeceleste.celestehomes.commands;

import com.redeceleste.celestehomes.managers.ConfigManager;
import com.redeceleste.celestehomes.managers.InventoryManager;
import com.redeceleste.celestehomes.managers.PermissionManager;
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
            if (!PermissionManager.hasAdmin(p)) {
                p.sendMessage(ConfigManager.NoPermission);
                return false;
            }

            if (args.length != 1) {
                p.sendMessage(ConfigManager.HomeInvalidArgument);
                return false;
            }

            if (args[0].equalsIgnoreCase("rl")) {
                ConfigManager.loadMessage();
                p.sendMessage(ConfigManager.Reload);
                return false;
            }

            try {
                InventoryManager.homeinventory(Bukkit.getPlayer(args[0]), p);
            } catch (Exception ignored) {
                p.sendMessage(ConfigManager.PlayerNotFound);
            }
        }
        return false;
    }
}
