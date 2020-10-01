package com.redeceleste.celesteshop.command.impl;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.command.Command;
import com.redeceleste.celesteshop.command.CommandArgument;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.manager.PointsManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PointsCommand extends Command {
    private final Main main;
    private final PointsManager points;
    private final ConfigManager config;
    private final ChatUtil chat;

    public PointsCommand(Main main) {
        super("points","point", "pontos", "ponto", "cashs", "cash");
        this.main = main;
        this.points = main.getPointsFactory().getManager();
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                chat.send(sender, "This command cannot be executed from the console", false);
            }

            Integer points = this.points.getPoints(sender.getName(), true);
            chat.send(sender, "Message.LookPoints",
                    chat.build("%points%", points));
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
                chat.send(sender, "This command cannot be executed from the console", false);
                return;
            }

            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            argument.execute(sender, newArgs);
            return;
        }

        if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Integer points = this.points.getPoints(args[0], true);
                chat.send(sender, "Message.LookPointsOtherPlayers",
                        chat.build("%player%", args[0]),
                        chat.build("%points%", points));
                return;
            }

            Integer points = this.points.getPoints(args[0], false);
            if (points != null) {
                chat.send(sender, "Message.LookPointsOtherPlayers",
                        chat.build("%player%", args[0]),
                        chat.build("%points%", points));
                return;
            }

            chat.send(sender, "Message.LookPlayerNotFound");
            return;
        }

        if (sender.hasPermission(config.get("admin_permission", ConfigType.config).toString())) {
            chat.send(sender, "Message.HelpAdmin");
            return;
        }

        chat.send(sender, "Message.Help");
    }
}