package com.redeceleste.celesteshop.command.impl.args.admin;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.command.CommandArgument;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.manager.PointsManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

public class RemoveCommandArgument extends CommandArgument {
    private final Main main;
    private final PointsManager points;
    private final ConfigManager config;
    private final ChatUtil chat;

    public RemoveCommandArgument(Main main) {
        super(false, "remove", "remover");
        this.main = main;
        this.points = main.getPointsFactory().getManager();
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(config.get("Admin_Permission", ConfigType.config).toString())) {
            chat.send(sender, "NoPermission", ConfigType.config);
            return;
        }

        if (!(args.length == 2) || Pattern.compile("[^0-9]").matcher(args[1]).find() || Integer.parseInt(args[1]) < 1) {
            chat.send(sender, "Message.RemovePointsInvalidArgument");
            return;
        }

        if (Bukkit.getPlayer(args[0]) != null) {
            points.removePoints(sender, args[0], Integer.valueOf(args[1]),true);
            return;
        }

        Integer pointsPlayer = points.getPoints(args[0], false);
        if (pointsPlayer != null) {
            points.removePoints(sender, args[0], Integer.valueOf(args[1]),false);
            return;
        }

        chat.send(sender, "Message.RemovePlayerNotFound");
    }
}
