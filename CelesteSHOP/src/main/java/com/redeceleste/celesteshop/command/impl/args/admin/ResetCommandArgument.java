package com.redeceleste.celesteshop.command.impl.args.admin;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.command.CommandArgument;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.manager.PointsManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ResetCommandArgument extends CommandArgument {
    private final Main main;
    private final PointsManager points;
    private final ConfigManager config;
    private final ChatUtil chat;

    public ResetCommandArgument(Main main) {
        super(false, "reset", "resetar");
        this.main = main;
        this.points = main.getPointsFactory().getManager();
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(config.getConfig("Admin_Permission").toString())) {
            chat.send(sender, "NoPermission", ConfigType.config);
            return;
        }

        if (!(args.length == 1)) {
            chat.send(sender, "Message.ResetPointsInvalidArgument");
            return;
        }

        if (Bukkit.getPlayer(args[0]) != null) {
            points.resetPoints(sender, args[0],true);
            return;
        }

        Integer pointsPlayer = points.getPoints(args[0], false);
        if (pointsPlayer != null) {
            points.resetPoints(sender, args[0],false);
            return;
        }

        chat.send(sender, "Message.ResetPlayerNotFound");
    }
}
