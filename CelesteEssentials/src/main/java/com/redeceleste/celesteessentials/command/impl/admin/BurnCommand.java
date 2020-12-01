package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class BurnCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public BurnCommand(CelesteEssentials main) {
        super("burn", "fogo", "incendiar");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String burnPerm = config.getConfig("Permission.Burn");
        if (!sender.hasPermission(perm) && !sender.hasPermission(burnPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 2 || Pattern.compile("[^0-9]").matcher(args[1]).find() || Integer.parseInt(args[1]) < 1) {
            chat.send(sender, "Burn.Invalid-Argument");
            bar.send(sender, "Burn.Invalid-Argument-Bar");
            title.send(sender, "Burn.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Burn.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Burn.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Burn.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        int time = Integer.parseInt(args[1]) * 20;
        t.setFireTicks(time);

        chat.send(sender, "Burn.Success",
                chat.build("{player}", t.getName()),
                chat.build("{time}", args[1]));
        bar.send(sender, "Burn.Success-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{time}", args[1]));
        title.send(sender, "Burn.Success-Title",
                chat.build("{player}", t.getName()),
                chat.build("{time}", args[1]));

        chat.send(t, "Burn.Receive",
                chat.build("{executor}", sender.getName()),
                chat.build("{time}", args[1]));
        bar.send(t, "Burn.Receive-Bar",
                chat.build("{executor}", sender.getName()),
                chat.build("{time}", args[1]));
        title.send(t, "Burn.Receive-Title",
                chat.build("{executor}", sender.getName()),
                chat.build("{time}", args[1]));
        return false;
    }
}
