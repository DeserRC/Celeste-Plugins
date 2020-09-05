package com.redeceleste.celestehomes.command.impls;

import com.redeceleste.celestehomes.command.CreateCommand;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.manager.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomesCommand extends CreateCommand {

    public HomesCommand() {
        super("homes");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p = (Player) sender;

        if (args.length != 0) {
            p.sendMessage(ConfigManager.HomeInvalidArgument);
            return false;
        }

        InventoryManager.homeinventory(p, Bukkit.getPlayer(args[0]));
        return false;
    }
}
