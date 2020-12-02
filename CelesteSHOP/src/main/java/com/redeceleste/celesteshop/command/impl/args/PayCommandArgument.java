package com.redeceleste.celesteshop.command.impl.args;

import com.redeceleste.celesteshop.CelesteSHOP;
import com.redeceleste.celesteshop.command.CommandArgument;
import com.redeceleste.celesteshop.manager.PointsManager;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

public class PayCommandArgument extends CommandArgument {
    private final CelesteSHOP main;
    private final PointsManager points;
    private final ChatUtil chat;

    public PayCommandArgument(CelesteSHOP main) {
        super(true, "pay","payment", "enviar");
        this.main = main;
        this.points = main.getPointsFactory().getManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(args.length == 2) || Pattern.compile("[^0-9]").matcher(args[1]).find() || args[1].length() > 9 || Integer.parseInt(args[1]) < 1) {
            chat.send(sender, "Message.PayPointsInvalidArgument");
            return;
        }

        if (sender.getName().equalsIgnoreCase(args[0])) {
            chat.send(sender, "Message.PayPointsSentHimself");
            return;
        }

        Integer pointsPlayer = points.getPoints(sender.getName(), true);
        if (pointsPlayer < Integer.parseInt(args[1])) {
            chat.send(sender, "Message.PayInsufficientPoints",
                    chat.build("%points%", pointsPlayer));
            return;
        }

        if (Bukkit.getPlayer(args[0]) != null) {
            points.payPoints(sender, args[0], Integer.valueOf(args[1]), true);
            return;
        }

        Integer pointsTarget = points.getPoints(args[0], false);
        if (pointsTarget != null) {
            points.payPoints(sender, args[0], Integer.valueOf(args[1]),false);
            return;
        }

        chat.send(sender, "Message.PayPlayerNotFound");
    }
}
