package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.manager.GodManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final GodManager god;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public GodCommand(CelesteEssentials main) {
        super("god", "deus");
        this.main = main;
        this.config = main.getConfigManager();
        this.god = main.getGodManager();
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
            String godPerm = config.getConfig("Permission.God");
            if (!p.hasPermission(perm) && !p.hasPermission(godPerm)) {
                chat.send(p, "No-Permission.Admin");
                return false;
            }

            String mode;
            if (god.isGodMode(p)) {
                god.setGodMode(p, false);
                mode = config.getMessage("Disable");
            } else {
                god.setGodMode(p, true);
                mode = config.getMessage("Enable");
            }

            chat.send(p, "God.Success",
                    chat.build("{mode}", mode));
            bar.send(p, "God.Success-Bar",
                    chat.build("{mode}", mode));
            title.send(p, "God.Success-Title",
                    chat.build("{mode}", mode));
            return false;
        }

        String perm = config.getConfig("Permission.Admin");
        String godPerm = config.getConfig("Permission.God-Other-Players");
        if (!sender.hasPermission(perm) && !sender.hasPermission(godPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "God.Invalid-Argument");
            bar.send(sender, "God.Invalid-Argument-Bar");
            title.send(sender, "God.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "God.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "God.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "God.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        String mode;
        if (god.isGodMode(t)) {
            god.setGodMode(t, false);
            mode = config.getMessage("Disable");
        } else {
            god.setGodMode(t, true);
            mode = config.getMessage("Enable");
        }

        chat.send(sender, "God.Success-Other-Player",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode));
        bar.send(sender, "God.Success-Other-Player-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode));
        title.send(sender, "God.Success-Other-Player-Title",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode));

        chat.send(t, "God.Receive",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode));
        bar.send(t, "God.Receive-Bar",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode));
        title.send(t, "God.Receive-Title",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode));
        return false;
    }
}
