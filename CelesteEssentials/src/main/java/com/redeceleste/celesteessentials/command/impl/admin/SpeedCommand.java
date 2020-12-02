package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.regex.Pattern;

public class SpeedCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public SpeedCommand(CelesteEssentials main) {
        super("speed", "velocidade");
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
        String perm = config.getConfig("Permission.Admin");
        String speedPerm = config.getConfig("Permission.Speed");
        if (!p.hasPermission(perm) && !p.hasPermission(speedPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 2 || Pattern.compile("[^0-9]").matcher(args[1]).find() || args[1].length() > 9 || Integer.parseInt(args[1]) < 0 || Integer.parseInt(args[1]) > 10) {
            chat.send(p, "Speed.Invalid-Argument");
            bar.send(p, "Speed.Invalid-Argument-Bar");
            title.send(p, "Speed.Invalid-Argument-Title");
            return false;
        }

        setupArguments(p, args);
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

        chat.send(sender, "Speed.Invalid-Argument");
        bar.send(sender, "Speed.Invalid-Argument-Bar");
        title.send(sender, "Speed.Invalid-Argument-Title");
    }
}
