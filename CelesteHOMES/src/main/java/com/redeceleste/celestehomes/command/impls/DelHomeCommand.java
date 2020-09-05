package com.redeceleste.celestehomes.command.impls;

import com.redeceleste.celestehomes.command.CreateCommand;
import com.redeceleste.celestehomes.manager.HomeManager;
import com.redeceleste.celestehomes.manager.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand extends CreateCommand {
    public DelHomeCommand() {
        super("delhome", "deletehome", "deletarhome");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p = (Player) sender;

        if (args.length != 1) {
            p.sendMessage(ConfigManager.DelHomeArgumentsInvalid);
            return false;
        }

        if (!HomeManager.isHome(p, args[0])) {
            p.sendMessage(ConfigManager.DelHomeNotFound);
            return false;
        }

        HomeManager.delHome(p, args[0]);
        p.sendMessage(ConfigManager.HomeSucessDeleted);
        return false;
    }
}
