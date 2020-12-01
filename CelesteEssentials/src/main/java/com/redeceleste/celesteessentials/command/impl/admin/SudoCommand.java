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

import java.util.StringJoiner;

public class SudoCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public SudoCommand(CelesteEssentials main) {
        super("sudo", "forcar");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String sudoPerm = config.getConfig("Permission.Sudo");
        if (!sender.hasPermission(perm) && !sender.hasPermission(sudoPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length < 2) {
            chat.send(sender, "Sudo.Invalid-Argument");
            bar.send(sender, "Sudo.Invalid-Argument-Bar");
            title.send(sender, "Sudo.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Sudo.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Sudo.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Sudo.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        StringJoiner sj = new StringJoiner(" ");
        for (int i=1; i<args.length; i++) {
            sj.add(args[i]);
        }

        Bukkit.dispatchCommand(t, sj.toString());

        chat.send(sender, "Sudo.Success",
                chat.build("{player}", t.getName()),
                chat.build("{command}", sj));
        bar.send(sender, "Sudo.Success-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{command}", sj));
        title.send(sender, "Sudo.Success-Title",
                chat.build("{player}", t.getName()),
                chat.build("{command}", sj));
        return false;
    }
}
