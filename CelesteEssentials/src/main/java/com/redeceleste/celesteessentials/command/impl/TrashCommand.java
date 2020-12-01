package com.redeceleste.celesteessentials.command.impl;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TrashCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public TrashCommand(CelesteEssentials main) {
        super("trash", "lixo", "lixeira");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            chat.send(sender, "This command cannot be executed via the console");
            return false;
        }

        Player p = (Player) sender;
        if (args.length != 0) {
            chat.send(p, "Trash.Invalid-Argument");
            bar.send(p, "Trash.Invalid-Argument-Bar");
            title.send(p, "Trash.Invalid-Argument-Title");
            return false;
        }

        String title = config.getConfig("Inventory-Trash.Title");
        int size = config.getConfig("Inventory-Trash.Size");
        Inventory inv = Bukkit.createInventory(null, size, title);
        p.openInventory(inv);
        return false;
    }
}
