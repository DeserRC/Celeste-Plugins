package com.redeceleste.celestehomes.command.impls;

import com.redeceleste.celestehomes.command.CreateCommand;
import com.redeceleste.celestehomes.manager.HomeManager;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.manager.PermissionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand extends CreateCommand {

    public SetHomeCommand() {
        super("sethome","setarhome");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p = (Player) sender;

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
        if (pn.equals("0")) {
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
        return false;
    }
}
