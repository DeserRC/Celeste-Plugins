package com.redeceleste.celestehomes.command.impls;

import com.redeceleste.celestehomes.command.Command;
import com.redeceleste.celestehomes.manager.HomeManager;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.manager.InventoryManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand extends Command {
    public HomeCommand() {
        super("home");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p = (Player) sender;

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
        return false;
    }
}
