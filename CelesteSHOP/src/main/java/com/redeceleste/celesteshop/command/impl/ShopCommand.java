package com.redeceleste.celesteshop.command.impl;

import com.redeceleste.celesteshop.CelesteSHOP;
import com.redeceleste.celesteshop.command.Command;
import com.redeceleste.celesteshop.manager.InventoryManager;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand extends Command {
    private final CelesteSHOP main;
    private final InventoryManager inventory;
    private final ChatUtil chat;

    public ShopCommand(CelesteSHOP main) {
        super("shop", "vip", "buy");
        this.main = main;
        this.inventory = main.getInventoryManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            chat.send(sender, "This command cannot be executed from the console");
            return false;
        }

        Player p = (Player) sender;
        inventory.openInventory(p);
        return false;
    }
}
