package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ExpCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public ExpCommand(CelesteEssentials main) {
        super("exp", "xp");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String expPerm = config.getConfig("Permission.Exp");
        if (!sender.hasPermission(perm) && !sender.hasPermission(expPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length < 2) {
            chat.send(sender, "Exp.Invalid-Argument");
            bar.send(sender, "Exp.Invalid-Argument-Bar");
            title.send(sender, "Exp.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[1]);
        if (t == null) {
            chat.send(sender, "Exp.Player-Not-Found",
                    chat.build("{player}", args[1]));
            bar.send(sender, "Exp.Player-Not-Found-Bar",
                    chat.build("{player}", args[1]));
            title.send(sender, "Exp.Player-Not-Found-Title",
                    chat.build("{player}", args[1]));
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

        chat.send(sender, "Exp.Invalid-Argument");
        bar.send(sender, "Exp.Invalid-Argument-Bar");
        title.send(sender, "Exp.Invalid-Argument-Title");
    }
}
