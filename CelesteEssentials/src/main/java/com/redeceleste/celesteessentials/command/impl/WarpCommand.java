package com.redeceleste.celesteessentials.command.impl;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.manager.InventoryManager;
import com.redeceleste.celesteessentials.manager.WarpManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class WarpCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final InventoryManager inv;
    private final WarpManager warp;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public WarpCommand(CelesteEssentials main) {
        super("warp", "local");
        this.main = main;
        this.config = main.getConfigManager();
        this.inv = main.getInventoryManager();
        this.warp = main.getWarpManager();
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
        if (args.length == 0) {
            inv.openWarp(p);
            return false;
        }

        String perm = config.getConfig("Permission.Admin");
        String warpPerm = config.getConfig("Permission.Warp");
        if (!p.hasPermission(perm) && !p.hasPermission(warpPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        setupArguments(sender, args);
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
                chat.send(sender, "This command cannot be executed via the console");
                return;
            }

            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            argument.execute(sender, newArgs);
            return;
        }

        if (args.length == 1 && warp.warpExist(args[0])) {
            Boolean teleport = warp.teleportPlayer((Player) sender, args[0]);
            if (teleport == null) {
                chat.send(sender, "Warp.Warp-Not-Found",
                        chat.build("{warp}", args[0]));
                bar.send(sender, "Warp.Warp-Not-Found-Bar",
                        chat.build("{warp}", args[0]));
                title.send(sender, "Warp.Warp-Not-Found-Title",
                        chat.build("{warp}", args[0]));
            }

            if (!teleport) chat.send(sender, "No-Permission.Warp",
                    chat.build("{warp}", args[0].toUpperCase()));
            return;
        }

        chat.send(sender, "Warp.Invalid-Argument");
        bar.send(sender, "Warp.Invalid-Argument-Bar");
        title.send(sender, "Warp.Invalid-Argument-Title");
    }
}
