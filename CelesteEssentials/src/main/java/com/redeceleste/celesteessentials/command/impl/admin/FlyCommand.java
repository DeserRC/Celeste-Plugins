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

public class FlyCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public FlyCommand(CelesteEssentials main) {
        super("fly", "voar");
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
            String flyPerm = config.getConfig("Permission.Fly");
            if (!p.hasPermission(perm) && !p.hasPermission(flyPerm)) {
                chat.send(p, "No-Permission.Admin");
                return false;
            }

            String mode;
            if (p.getAllowFlight()) {
                p.setFlying(false);
                p.setAllowFlight(false);
                mode = config.getMessage("Disable");
            } else {
                p.setAllowFlight(true);
                mode = config.getMessage("Enable");
            }

            chat.send(p, "Fly.Success",
                    chat.build("{mode}", mode));
            bar.send(p, "Fly.Success-Bar",
                    chat.build("{mode}", mode));
            title.send(p, "Fly.Success-Title",
                    chat.build("{mode}", mode));
            return false;
        }

        String perm = config.getConfig("Permission.Admin");
        String flyPerm = config.getConfig("Permission.Fly-Other-Players");
        if (!sender.hasPermission(perm) && !sender.hasPermission(flyPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "Fly.Invalid-Argument");
            bar.send(sender, "Fly.Invalid-Argument-Bar");
            title.send(sender, "Fly.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Fly.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Fly.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Fly.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        String mode;
        if (t.getAllowFlight()) {
            t.setFlying(false);
            t.setAllowFlight(false);
            mode = config.getMessage("Disable");
        } else {
            t.setAllowFlight(true);
            mode = config.getMessage("Enable");
        }

        chat.send(sender, "Fly.Success-Other-Player",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode));
        bar.send(sender, "Fly.Success-Other-Player-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode));
        title.send(sender, "Fly.Success-Other-Player-Title",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode));

        chat.send(t, "Fly.Receive",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode));
        bar.send(t, "Fly.Receive-Bar",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode));
        title.send(t, "Fly.Receive-Title",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode));
        return false;
    }
}
