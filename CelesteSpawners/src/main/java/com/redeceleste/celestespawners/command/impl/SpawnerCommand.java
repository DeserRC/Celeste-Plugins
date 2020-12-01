package com.redeceleste.celestespawners.command.impl;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.command.Command;
import com.redeceleste.celestespawners.command.CommandArgument;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.InventoryManager;
import com.redeceleste.celestespawners.util.impl.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SpawnerCommand extends Command {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final InventoryManager inventory;
    private final ChatUtil chat;

    public SpawnerCommand(CelesteSpawners main) {
        super("spawner", "spawners");
        this.main = main;
        this.config = main.getConfigManager();
        this.inventory = main.getInventoryManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length != 0) {
            setupArguments(sender, args);
            return false;
        }

        if (!(sender instanceof Player)) {
            chat.send(sender, "The console may not execute this command");
            return false;
        }

        inventory.open((Player) sender, 1);
        return false;
    }

    private void setupArguments(CommandSender sender, String[] args) {
        CommandArgument argument = null;
        for (CommandArgument commandArgument : getArguments()) {
            if (commandArgument.getArgumentName().equalsIgnoreCase(args[0]) || Arrays.stream(commandArgument.getArgumentAliases()).anyMatch(args[0]::equalsIgnoreCase)) {
                argument = commandArgument;
            }
        }

        if (argument != null) {
            if (argument.getIsPlayerExclusive() && !(sender instanceof Player)) {
                chat.send(sender, "The console may not execute this command");
                return;
            }

            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            argument.execute(sender, newArgs);
            return;
        }

        String perm = config.getConfig("Permission.Admin");
        if (sender.hasPermission(perm)) {
            chat.send(sender, "Help.Message-Admin");
            return;
        }
        chat.send(sender, "Help.Message");
    }
}