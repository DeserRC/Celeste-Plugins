package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;
import java.util.regex.Pattern;

import static com.redeceleste.celesteessentials.util.LocationUtil.getLocation;
import static com.redeceleste.celesteessentials.util.LocationUtil.serialize;
import static java.util.Arrays.copyOfRange;

public class TeleportCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public TeleportCommand(CelesteEssentials main) {
        super("teleport", "tp");
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
        String teleportPerm = config.getConfig("Permission.Teleport");
        if (!p.hasPermission(perm) && !p.hasPermission(teleportPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        boolean invalid = false;
        boolean notFound0 = false;
        boolean notFound1 = false;
        String target = null;

        Player t0 = null;
        Player t1 = null;
        StringJoiner sj = new StringJoiner("-");

        if (args.length > 0) t0 = Bukkit.getPlayer(args[0]);

        switch (args.length) {
            case 1: {
                if (t0 == null) {
                    notFound0 = true;
                    break; }

                p.teleport(t0);
                target = t0.getName();
                break; }
            case 2: {
                if (t0 == null) {
                    notFound0 = true;
                    break; }

                t1 = Bukkit.getPlayer(args[1]);
                if (t1 == null) {
                    notFound1 = true;
                    break; }

                t0.teleport(t1);
                target = t1.getName();
                break; }
            case 3: {
                if (Pattern.compile("[^0-9~]").matcher(args[0]).find() || Pattern.compile("[^0-9~]").matcher(args[1]).find() || Pattern.compile("[^0-9~]").matcher(args[2]).find()) {
                    invalid = true;
                    break; }

                t0 = p;
                Location loc = getLocation(p.getLocation(), args);
                String location = serialize(loc, false);
                String[] split = location.split(":");
                for (int i=1; i<split.length; i++) {
                    sj.add(split[i].split("\\.") [0]);
                }

                target = sj.toString();
                p.teleport(loc);
                break; }
            case 4: {
                if (Pattern.compile("[^0-9~]").matcher(args[1]).find() || Pattern.compile("[^0-9~]").matcher(args[2]).find() || Pattern.compile("[^0-9~]").matcher(args[3]).find()) {
                    invalid = true;
                    break; }
                
                if (t0 == null) {
                    notFound0 = true;
                    break; }

                args = copyOfRange(args, 1, args.length);
                Location loc = getLocation(t0.getLocation(), args);
                String location = serialize(loc, false);
                String[] split = location.split(":");
                for (int i=1; i<split.length; i++) {
                    sj.add(split[i].split("\\.") [0]);
                }

                target = sj.toString();
                t0.teleport(loc);
                break; }
            default: invalid = true;
        }

        if (invalid) {
            chat.send(p, "Teleport.Invalid-Argument");
            bar.send(p, "Teleport.Invalid-Argument-Bar");
            title.send(p, "Teleport.Invalid-Argument-Title");
            return false;
        }

        if (notFound0 || notFound1) {
            if (notFound1) args[0] = args[1];
            chat.send(p, "Teleport.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(p, "Teleport.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(p, "Teleport.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        if (args.length == 1 || args.length == 3) {
            chat.send(p, "Teleport.Success",
                    chat.build("{target}", target));
            bar.send(p, "Teleport.Success-Bar",
                    chat.build("{target}", target));
            title.send(p, "Teleport.Success-Title",
                    chat.build("{target}", target));
            return false;
        }

        chat.send(p, "Teleport.Success-Other-Player",
                chat.build("{player}", t0.getName()),
                chat.build("{target}", target));
        bar.send(p, "Teleport.Success-Other-Player-Bar",
                chat.build("{player}", t0.getName()),
                chat.build("{target}", target));
        title.send(p, "Teleport.Success-Other-Player-Title",
                chat.build("{player}", t0.getName()),
                chat.build("{target}", target));
        return false;
    }
}
