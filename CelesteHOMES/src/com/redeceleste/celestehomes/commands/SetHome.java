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
            switch (args.length) {
                case 1:
                    if (ConfigManager.BlackList.contains(p.getWorld().getName())) {
                        if (args[0].length() > Integer.parseInt(ConfigManager.MinimumCharactersHome)) {
                            String pn = PermissionManager.getPermission(p);
                            if (p.hasPermission(ConfigManager.Permission + pn)) {
                                if (PermissionManager.remainingHomes(p, Integer.parseInt(pn)) > 0) {
                                    if (!HomeManager.isHome(p, args[0])) {
                                        HomeManager.setHome(p, args[0]);
                                        p.sendMessage(ConfigManager.HomeSucessCreate);
                                    } else {
                                        p.sendMessage(ConfigManager.ContainsHome);
                                    }
                                } else {
                                    p.sendMessage(ConfigManager.HomeLimitReached);
                                }
                            } else {
                                p.sendMessage(ConfigManager.NoPermission);
                            }
                        } else {
                            p.sendMessage(ConfigManager.FewCharacters);
                        }
                    } else {
                        p.sendMessage(ConfigManager.BlackListWorldMessage);
                    }
                    return false;
                default:
                    p.sendMessage(ConfigManager.SetHomeArgumentsInvalid);
            }
        }
        return false;
    }
}
