package com.redeceleste.celestehomes.command.impls.admin;

import com.redeceleste.celestehomes.command.CreateCommand;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.manager.InventoryManager;
import com.redeceleste.celestehomes.manager.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AHomeCommand extends CreateCommand {
    public AHomeCommand() {
        super("ahome", "adminhome", "homeadmin");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p = (Player) sender;

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
        return false;
    }
}
