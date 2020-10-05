package com.redeceleste.celesteshop.listener;

import com.redeceleste.celesteshop.CelesteSHOP;
import com.redeceleste.celesteshop.dao.PointsDAO;
import com.redeceleste.celesteshop.event.impl.*;
import com.redeceleste.celesteshop.factory.PointsFactory;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.model.impl.Points;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import com.redeceleste.celesteshop.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PointsListener implements Listener {
    private final CelesteSHOP main;
    private final PointsFactory factory;
    private final PointsDAO dao;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final TitleUtil title;

    public PointsListener(CelesteSHOP main) {
        this.main = main;
        this.factory = main.getPointsFactory();
        this.dao = main.getConnectionFactory().getDao();
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.title = main.getMessageFactory().getTitle();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onPointsPay(PointsPayEvent e) {
        if (e.isCancelled()) return;

        if (!e.getOnline()) {
            dao.replace(new Points(e.getTarget(), e.getTargetValue() + e.getValue()), true);
            factory.getPoints().put(e.getPlayer().getName().toLowerCase(), new Points(e.getPlayer().getName(),e.getPlayerValue() - e.getValue()));

            chat.send(e.getPlayer(), "Message.PaySucessSent",
                    chat.build("%player%", e.getTarget()),
                    chat.build("%points%", e.getValue()));
            return;
        }

        if (!factory.getUpdate().contains(e.getPlayer().getName().toLowerCase())) {
            factory.getUpdate().put(e.getPlayer().getName().toLowerCase());
        }

        if (!factory.getUpdate().contains(e.getTarget().toLowerCase())) {
            factory.getUpdate().put(e.getTarget().toLowerCase());
        }

        Integer pValue = e.getPlayerValue()-e.getValue();
        Integer tValue = e.getTargetValue()+e.getValue();
        factory.getPoints().put(e.getPlayer().getName().toLowerCase(), new Points(e.getPlayer().getName(), pValue));
        factory.getPoints().put(e.getTarget().toLowerCase(), new Points(e.getTarget(), tValue));

        CommandSender p = e.getPlayer();
        Player t = Bukkit.getPlayer(e.getTarget());

        chat.send(p, "Message.PaySucessSent",
                chat.build("%player%", t.getName()),
                chat.build("%points%", e.getValue()));

        chat.send(t, "Message.PayPointsReceived",
                chat.build("%player%", p.getName()),
                chat.build("%points%", e.getValue()));

        title.send(t, "Message.PayPointsReceivedTitle",
                title.build("%player%", p.getName()),
                title.build("%points%", e.getValue()));
    }

    @EventHandler
    public void onPointsAdd(PointsAddEvent e) {
        if (e.isCancelled()) return;

        if (!e.getOnline()) {
            dao.replace(new Points(e.getTarget(), e.getTargetValue() + e.getValue()), true);

            chat.send(e.getPlayer(), "Message.AddSucessSent",
                    chat.build("%player%", e.getTarget()),
                    chat.build("%points%", e.getValue()));
            return;
        }

        if (!factory.getUpdate().contains(e.getTarget().toLowerCase())) {
            factory.getUpdate().put(e.getTarget().toLowerCase());
        }

        Integer value = e.getTargetValue()+e.getValue();
        factory.getPoints().put(e.getTarget().toLowerCase(), new Points(e.getTarget(), value));

        CommandSender p = e.getPlayer();
        Player t = Bukkit.getPlayer(e.getTarget());

        chat.send(p, "Message.AddSucessSent",
                chat.build("%player%", t.getName()),
                chat.build("%points%", e.getValue()));

        chat.send(t, "Message.AddPointsReceived",
                chat.build("%points%", e.getValue()));

        title.send(t, "Message.AddPointsReceivedTitle",
                title.build("%points%", e.getValue()));
    }

    @EventHandler
    public void onPointsRemove(PointsRemoveEvent e) {
        if (e.isCancelled()) return;

        if (!e.getOnline()) {
            dao.replace(new Points(e.getTarget(), e.getTargetValue() - e.getValue()), true);

            chat.send(e.getPlayer(), "Message.RemoveSucessSent",
                    chat.build("%player%", e.getTarget()),
                    chat.build("%points%", e.getValue()));
            return;
        }

        if (!factory.getUpdate().contains(e.getTarget().toLowerCase())) {
            factory.getUpdate().put(e.getTarget().toLowerCase());
        }

        Integer value = e.getTargetValue()-e.getValue();
        factory.getPoints().put(e.getTarget().toLowerCase(), new Points(e.getTarget(), value));

        CommandSender p = e.getPlayer();
        Player t = Bukkit.getPlayer(e.getTarget());

        chat.send(p, "Message.RemoveSucessSent",
                chat.build("%player%", t.getName()),
                chat.build("%points%", e.getValue()));

        chat.send(t, "Message.RemovePointsReceived",
                chat.build("%points%", e.getValue()));

        title.send(t, "Message.RemovePointsReceivedTitle",
                title.build("%points%", e.getValue()));
    }

    @EventHandler
    public void onPointsSet(PointsSetEvent e) {
        if (e.isCancelled()) return;

        if (!e.getOnline()) {
            dao.replace(new Points(e.getTarget(), e.getValue()), true);

            chat.send(e.getPlayer(), "Message.SetSucessSent",
                    chat.build("%player%", e.getTarget()),
                    chat.build("%points%", e.getValue()));
            return;
        }

        if (!factory.getUpdate().contains(e.getTarget().toLowerCase())) {
            factory.getUpdate().put(e.getTarget().toLowerCase());
        }

        factory.getPoints().put(e.getTarget().toLowerCase(), new Points(e.getTarget(), e.getValue()));

        CommandSender p = e.getPlayer();
        Player t = Bukkit.getPlayer(e.getTarget());

        chat.send(p, "Message.SetSucessSent",
                chat.build("%player%", t.getName()),
                chat.build("%points%", e.getValue()));

        chat.send(t, "Message.SetPointsReceived",
                chat.build("%points%", e.getValue()));

        title.send(t, "Message.SetPointsReceivedTitle",
                title.build("%points%", e.getValue()));
    }

    @EventHandler
    public void onPointsReset(PointsResetEvent e) {
        if (e.isCancelled()) return;

        if (!e.getOnline()) {
            dao.replace(new Points(e.getTarget(), 0), true);

            chat.send(e.getPlayer(), "Message.ResetSucessSent",
                    chat.build("%player%", e.getTarget()));
            return;
        }

        if (!factory.getUpdate().contains(e.getTarget().toLowerCase())) {
            factory.getUpdate().put(e.getTarget().toLowerCase());
        }

        factory.getPoints().put(e.getTarget().toLowerCase(), new Points(e.getTarget(), 0));

        CommandSender p = e.getPlayer();
        Player t = Bukkit.getPlayer(e.getTarget());

        chat.send(p, "Message.ResetSucessSent",
                chat.build("%player%", t.getName()),
                chat.build("%player%", t.getName()));

        chat.send(t, "Message.ResetPointsReceived");

        title.send(t, "Message.ResetPointsReceivedTitle");
    }

    @EventHandler
    public void onPointsPurchase(PointsPurchaseEvent e) {
        if (e.isCancelled()) return;

        if (!e.getOnline()) {
            dao.replace(new Points(e.getTarget(), e.getTargetValue() + e.getValue()), true);

            String message = config.getMessage("Message.BuyPointsBroadCast");
            message = message.replace("%player%", e.getTarget()).replace("%points%", e.getValue().toString());

            Bukkit.broadcastMessage(message);
        }

        if (!factory.getUpdate().contains(e.getTarget().toLowerCase())) {
            factory.getUpdate().put(e.getTarget().toLowerCase());
        }

        factory.getPoints().put(e.getTarget().toLowerCase(), new Points(e.getTarget(), e.getTargetValue()+e.getValue()));

        Player t = Bukkit.getPlayer(e.getTarget());

        String message = config.getMessage("Message.BuyPointsBroadCast");
        message = message.replace("%player%", t.getName()).replace("%points%", e.getValue().toString());

        Bukkit.broadcastMessage(message);

        chat.send(t, "Message.BuyPointsMessage",
                chat.build("%points%", e.getValue()));

        title.send(t, "Message.BuyPointsTitle");
    }
}
