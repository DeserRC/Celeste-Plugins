package com.redeceleste.celestespawners.command.impl.args;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.command.CommandArgument;
import com.redeceleste.celestespawners.manager.InventoryManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopArgument extends CommandArgument {
    private final CelesteSpawners main;
    private final InventoryManager inventory;

    public ShopArgument(CelesteSpawners main) {
        super(true, "shop", "loja");
        this.main = main;
        this.inventory = main.getInventoryManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        inventory.open((Player) sender, 1);
    }
}
