package com.redeceleste.celestehomes.commands;

import com.redeceleste.celestehomes.managers.HomeManager;
import com.redeceleste.celestehomes.managers.ConfigManager;
import com.redeceleste.celestehomes.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHome implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("sethome")) {
            if (args.length != 1) {
                p.sendMessage(ConfigManager.SetHomeArgumentsInvalid);
                return false;
            }

            if (!ConfigManager.BlackList.contains(p.getWorld().getName())) {
                p.sendMessage(ConfigManager.BlackListWorldMessage);
                return false;
            }

            if (args[0].length() < Integer.parseInt(ConfigManager.MinimumCharactersHome)) {
                p.sendMessage(ConfigManager.FewCharacters);
                return false;
            }

            String pn = PermissionManager.getPermission(p);
            if (!p.hasPermission(ConfigManager.Permission + pn)) {
                p.sendMessage(ConfigManager.NoPermission);
                return false;
            }

            if (PermissionManager.remainingHomes(p, Integer.parseInt(pn)) <= 0) {
                p.sendMessage(ConfigManager.HomeLimitReached);
                return false;
            }

            if (HomeManager.isHome(p, args[0])) {
                p.sendMessage(ConfigManager.ContainsHome);
                return false;
            }
            
            HomeManager.setHome(p, args[0]);
            p.sendMessage(ConfigManager.HomeSucessCreate);
        }
        return false;
    }
}
