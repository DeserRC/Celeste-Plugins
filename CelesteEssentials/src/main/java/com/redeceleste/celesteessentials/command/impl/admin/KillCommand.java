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

public class KillCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public KillCommand(CelesteEssentials main) {
        super("kill");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                chat.send(sender, "This command cannot be executed via the console");
                return false;
            }

            Player p = (Player) sender;
            String perm = config.getConfig("Permission.Admin");
            String killPerm = config.getConfig("Permission.Kill");
            if (!p.hasPermission(perm) && !p.hasPermission(killPerm)) {
                chat.send(p, "No-Permission.Admin");
                return false;
            }

            p.setHealth(0);
            chat.send(p, "Kill.Success");
            bar.send(p, "Kill.Success-Bar");
            title.send(p, "Kill.Success-Title");
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "Kill.Invalid-Argument");
            bar.send(sender, "Kill.Invalid-Argument-Bar");
            title.send(sender, "Kill.Invalid-Argument-Title");
            return false;
        }

        String perm = config.getConfig("Permission.Admin");
        String killPerm = config.getConfig("Permission.Kill-Other-Players");
        if (!sender.hasPermission(perm) && !sender.hasPermission(killPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Kill.Player-Not-Found");
            bar.send(sender, "Kill.Player-Not-Found-Bar");
            title.send(sender, "Kill.Player-Not-Found-Title");
            return false;
        }

        t.setHealth(0);
        chat.send(sender, "Kill.Success-Other-Player",
                chat.build("{player}", t.getName()));
        bar.send(sender, "Kill.Success-Other-Player-Bar",
                chat.build("{player}", t.getName()));
        title.send(sender, "Kill.Success-Other-Player-Title",
                chat.build("{player}", t.getName()));
        return false;
    }
}
