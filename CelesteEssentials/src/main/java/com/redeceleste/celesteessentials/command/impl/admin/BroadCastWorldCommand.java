package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BroadCastWorldCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public BroadCastWorldCommand(CelesteEssentials main) {
        super("broadcastworld", "bcw", "bcworld", "broadcastw", "bcastw", "alertaworld", "alertmundo", "alertaw", "alertworld", "alertw");
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

        if (args.length <= 1) {
            chat.send(sender, "BroadCast-World.Invalid-Argument");
            bar.send(sender, "BroadCast-World.Invalid-Argument-Bar");
            title.send(sender, "BroadCast-World.Invalid-Argument-Title");
            return false;
        }

        World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            chat.send(sender, "BroadCast-World.World-Not-Found",
                    chat.build("{world}", args[0]));
            bar.send(sender, "BroadCast-World.World-Not-Found-Bar",
                    chat.build("{world}", args[0]));
            title.send(sender, "BroadCast-World.World-Not-Found-Title",
                    chat.build("{world}", args[0]));
            return false;
        }

        StringJoiner sj = new StringJoiner(" ");
        for (int i=1; i<args.length; i++) {
            sj.add(args[i].replace("&", "\u00A7"));
        }

        List<String> message = config.getListMessage("BroadCast-World.Receive");
        message = message.stream().map(line -> line
                .replace("{executor}", sender.getName())
                .replace("{message}", sj.toString())).collect(Collectors.toList());

        chat.send(sender, "BroadCast-World.Success",
                chat.build("{message}", sj));
        bar.send(sender, "BroadCast-World.Success-Bar",
                chat.build("{message}", sj));
        title.send(sender, "BroadCast-World.Success-Title",
                chat.build("{message}", sj));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().equals(world)) continue;
            message.forEach(player::sendMessage);
        }
        return false;
    }
}
