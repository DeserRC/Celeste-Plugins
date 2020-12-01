package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class EssentialsCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;

    public EssentialsCommand(CelesteEssentials main) {
        super("essentials", "essential");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        setupArguments(sender, args);
        return false;
    }

    private void setupArguments(CommandSender sender, String[] args) {
        CommandArgument argument = null;
        for (CommandArgument commandArgument : getArguments()) {
            if (args.length == 0) break;
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

        String perm = config.getConfig("Permission.Admin");
        if (sender.hasPermission(perm)) {
            chat.send(sender, "Help.Message-Admin");
            return;
        }
        chat.send(sender, "Help.Message");
    }
}
