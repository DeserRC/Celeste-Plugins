package com.redeceleste.celesteshop.command.impl;

import com.redeceleste.celesteshop.CelesteSHOP;
import com.redeceleste.celesteshop.command.Command;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.manager.PointsManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class DrawPoints extends Command {
    private final CelesteSHOP main;
    private final PointsManager points;
    private final ConfigManager config;
    private final ChatUtil chat;
    private ScheduledFuture<?> scheduledFuture;

    public DrawPoints(CelesteSHOP main) {
        super("drawpoints", "drawpoint", "drawcashs", "drawcash", "sortearpontos", "sortearcashs");
        this.main = main;
        this.points = main.getPointsFactory().getManager();
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission(config.getConfig("Admin_Permission").toString())) {
            chat.send(sender, "NoPermission", ConfigType.config);
            return false;
        }

        if (!(args.length == 1) || Pattern.compile("[^0-9]").matcher(args[0]).find() || Integer.parseInt(args[0]) < 1) {
            chat.send(sender, "Message.DrawPointsInvalidArgument");
            return false;
        }

        chat.send(sender, "Message.DrawPointsStart");

        AtomicInteger time = new AtomicInteger(config.getConfig("DrawTime"));
        List<String> messageWin = config.getList("Message.DrawPointsWin", ConfigType.message);
        List<String> messageQueue = config.getList("Message.DrawPointsQueue", ConfigType.message);

        main.getScheduled().scheduleWithFixedDelay(() -> {
            if (time.get() == 0) {
                Player p = (Player) Bukkit.getOnlinePlayers().toArray()[new Random().nextInt(Bukkit.getOnlinePlayers().size())];
                messageWin.forEach(message -> Bukkit.broadcastMessage(message.replace("%player%", p.getName()).replace("%points%", args[0])));
                points.addPoints(Bukkit.getConsoleSender(), p.getName(), Integer.parseInt(args[0]), true);
                scheduledFuture.cancel(true);
            }
            messageQueue.forEach(message -> Bukkit.broadcastMessage(message.replace("%time%", time.toString())));
            time.getAndDecrement();
        }, 1, 1, TimeUnit.SECONDS);
        return false;
    }
}
