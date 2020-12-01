package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BroadCastCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public BroadCastCommand(CelesteEssentials main) {
        super("broadcast", "bc", "bcast", "alerta", "alert");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String bcPerm = config.getConfig("Permission.BroadCast");
        if (!sender.hasPermission(perm) && !sender.hasPermission(bcPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length == 0) {
            chat.send(sender, "BroadCast.Invalid-Argument");
            bar.send(sender, "BroadCast.Invalid-Argument-Bar");
            title.send(sender, "BroadCast.Invalid-Argument-Title");
            return false;
        }

        StringJoiner sj = new StringJoiner(" ");
        for (int i=0; i<args.length; i++) {
            sj.add(args[i].replace("&", "\u00A7"));
        }

        List<String> message = config.getListMessage("BroadCast.Receive");
        message = message.stream().map(line -> line
                .replace("{executor}", sender.getName())
                .replace("{message}", sj.toString())).collect(Collectors.toList());

        chat.send(sender, "BroadCast.Success",
                chat.build("{message}", sj));
        bar.send(sender, "BroadCast.Success-Bar",
                chat.build("{message}", sj));
        title.send(sender, "BroadCast.Success-Title",
                chat.build("{message}", sj));

        message.forEach(Bukkit::broadcastMessage);
        return false;
    }
}
