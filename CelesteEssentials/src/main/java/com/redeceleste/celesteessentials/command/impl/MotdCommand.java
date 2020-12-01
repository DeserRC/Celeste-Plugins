package com.redeceleste.celesteessentials.command.impl;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class MotdCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public MotdCommand(CelesteEssentials main) {
        super("motd");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length != 0) {
            chat.send(sender, "Motd.Invalid-Argument");
            bar.send(sender, "Motd.Invalid-Argument-Bar");
            title.send(sender, "Motd.Invalid-Argument-Title");
            return false;
        }

        boolean useMotd = config.getMessage("Motd.Success.Use");
        if (!useMotd) return false;

        List<String> message = config.getListMessage("Motd.Success.Message");
        message = message.stream().map(line -> line
                .replace("{player}", sender.getName())).collect(Collectors.toList());

        message.forEach(sender::sendMessage);
        return false;
    }
}
