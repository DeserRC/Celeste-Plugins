package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.regex.Pattern;

import static org.bukkit.potion.PotionEffectType.getById;
import static org.bukkit.potion.PotionEffectType.getByName;

public class EffectCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public EffectCommand(CelesteEssentials main) {
        super("effect", "efeito");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String effectPerm = config.getConfig("Permission.Effect");
        if (!sender.hasPermission(perm) && !sender.hasPermission(effectPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 4 || Pattern.compile("[^0-9]").matcher(args[2]).find() || Integer.parseInt(args[2]) < 1 || Pattern.compile("[^0-9]").matcher(args[3]).find() || Integer.parseInt(args[3]) < 1) {
            chat.send(sender, "Effect.Invalid-Argument");
            bar.send(sender, "Effect.Invalid-Argument-Bar");
            title.send(sender, "Effect.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        int duration = Integer.parseInt(args[2]);
        int amplifier = Integer.parseInt(args[3]);
        if (t == null) {
            chat.send(sender, "Effect.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Effect.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Effect.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        boolean notFound = false;
        PotionEffectType type = getByName(args[1]);
        if (type == null) {
            if (Pattern.matches("[0-9]", args[1])) {
                Integer id = Integer.parseInt(args[1]);
                type = getById(id);
                if (type == null) notFound = true;
            } else notFound = true;
        }

        if (notFound) {
            chat.send(sender, "Effect.Effect-Not-Found",
                    chat.build("{effect}", args[1]));
            bar.send(sender, "Effect.Effect-Not-Found-Bar",
                    chat.build("{effect}", args[1]));
            title.send(sender, "Effect.Effect-Not-Found-Title",
                    chat.build("{effect}", args[1]));
            return false;
        }

        t.addPotionEffect(new PotionEffect(type, duration, amplifier));
        chat.send(sender, "Effect.Success",
                chat.build("{player}", t.getName()),
                chat.build("{effect}", type.getName()),
                chat.build("{time}", duration),
                chat.build("{level}", amplifier));
        bar.send(sender, "Effect.Success-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{effect}", type.getName()),
                chat.build("{time}", duration),
                chat.build("{level}", amplifier));
        title.send(sender, "Effect.Success-Title",
                chat.build("{player}", t.getName()),
                chat.build("{effect}", type.getName()),
                chat.build("{time}", duration),
                chat.build("{level}", amplifier));

        chat.send(t, "Effect.Receive",
                chat.build("{executor}", sender.getName()),
                chat.build("{effect}", type.getName()),
                chat.build("{time}", duration),
                chat.build("{level}", amplifier));
        bar.send(t, "Effect.Receive-Bar",
                chat.build("{executor}", sender.getName()),
                chat.build("{effect}", type.getName()),
                chat.build("{time}", duration),
                chat.build("{level}", amplifier));
        title.send(t, "Effect.Receive-Title",
                chat.build("{executor}", sender.getName()),
                chat.build("{effect}", type.getName()),
                chat.build("{time}", duration),
                chat.build("{level}", amplifier));
        return false;
    }
}