package com.redeceleste.celesteessentials.command.impl;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.redeceleste.celesteessentials.util.ReflectionUtil.getNMS;
import static com.redeceleste.celesteessentials.util.ReflectionUtil.getOBC;

public class PingCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    private final Method getHandle;

    private final Field pingF;

    @SneakyThrows
    public PingCommand(CelesteEssentials main) {
        super("ping");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();

        Class<?> craftPlayerClass = getOBC("entity.CraftPlayer");
        Class<?> entityPlayerClass = getNMS("EntityPlayer");

        getHandle = craftPlayerClass.getMethod("getHandle");

        pingF = entityPlayerClass.getField("ping");
    }

    @Override @SneakyThrows
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                chat.send(sender, "This command cannot be executed via the console");
                return false;
            }

            Player p = (Player) sender;
            Object player = getHandle.invoke(p);
            String ping = String.valueOf(pingF.get(player));

            chat.send(sender, "Ping.Success",
                    chat.build("{ping}", ping));
            bar.send(sender, "Ping.Success-Bar",
                    chat.build("{ping}", ping));
            title.send(sender, "Ping.Success-Title",
                    chat.build("{ping}", ping));
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "Ping.Invalid-Argument");
            bar.send(sender, "Ping.Invalid-Argument-Bar");
            title.send(sender, "Ping.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Ping.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Ping.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Ping.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        Object player = getHandle.invoke(t);
        String ping = String.valueOf(pingF.get(player));

        chat.send(sender, "Ping.Success-Other-Player",
                chat.build("{player}", t.getName()),
                chat.build("{ping}", ping));
        bar.send(sender, "Ping.Success-Other-Player-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{ping}", ping));
        title.send(sender, "Ping.Success-Other-Player-Title",
                chat.build("{player}", t.getName()),
                chat.build("{ping}", ping));
        return false;
    }
}
