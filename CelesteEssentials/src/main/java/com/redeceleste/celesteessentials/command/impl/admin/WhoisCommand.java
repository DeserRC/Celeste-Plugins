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

import java.util.List;
import java.util.StringJoiner;

import static com.redeceleste.celesteessentials.util.DateUtil.formatDate;
import static com.redeceleste.celesteessentials.util.GeoIPUtil.getCountry;
import static com.redeceleste.celesteessentials.util.LocationUtil.serialize;
import static java.util.stream.Collectors.toList;

public class WhoisCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final GodManager god;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public WhoisCommand(CelesteEssentials main) {
        super("whois");
        this.main = main;
        this.config = main.getConfigManager();
        this.god = main.getGodManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String whoisPerm = config.getConfig("Permission.Whois");
        if (!sender.hasPermission(perm) && !sender.hasPermission(whoisPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "Whois.Invalid-Argument");
            bar.send(sender, "Whois.Invalid-Argument-Bar");
            title.send(sender, "Whois.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Whois.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Whois.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Whois.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        StringJoiner sj = new StringJoiner("-");
        String[] split = serialize(t.getLocation(), false).split(":");
        for (int i=0; i<split.length; i++) {
            sj.add(split[i].split("\\.") [0]);
        }

        String name      = t.getName();
        String heal      = String.valueOf(t.getHealth());
        String hunger    = String.valueOf(t.getFoodLevel());
        String exp       = String.valueOf(t.getLevel());
        String playTime  = formatDate(t.getLastPlayed());
        String location  = sj.toString();
        String ip        = t.getAddress().getAddress().toString().replace("/", "");
        String geo       = getCountry(t);
        String gamemode  = t.getGameMode().name();
        String isGod     = String.valueOf(god.isGodMode(t));
        String isOp      = String.valueOf(t.isOp());
        String fly       = String.valueOf(t.getAllowFlight());
        String speedWalk = String.valueOf(t.getWalkSpeed() * 10);
        String speedFly  = String.valueOf(t.getFlySpeed() * 10);

        List<String> message = config.getListMessage("Whois.Success");
        message = message.stream().map(line -> line
                .replace("{player}", name)
                .replace("{heal}", heal)
                .replace("{hunger}", hunger)
                .replace("{exp}", exp)
                .replace("{playtime}", playTime)
                .replace("{location}", location)
                .replace("{ip}", ip)
                .replace("{geo}", geo)
                .replace("{gamemode}", gamemode)
                .replace("{god}", isGod)
                .replace("{op}", isOp)
                .replace("{fly}", fly)
                .replace("{speedwalk}", speedWalk)
                .replace("{speedfly}", speedFly))
                .collect(toList());

        message.forEach(sender::sendMessage);
        return false;
    }
}
