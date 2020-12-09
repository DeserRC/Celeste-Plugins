package com.redeceleste.celestekits.command.impl.arg;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.builder.UserEventBuilder;
import com.redeceleste.celestekits.command.CommandArgument;
import com.redeceleste.celestekits.manager.ConfigManager;
import com.redeceleste.celestekits.manager.KitManager;
import com.redeceleste.celestekits.model.KitArgument;
import com.redeceleste.celestekits.util.impl.BarUtil;
import com.redeceleste.celestekits.util.impl.ChatUtil;
import com.redeceleste.celestekits.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitGiveArgument extends CommandArgument {
    private final CelesteKit main;
    private final ConfigManager config;
    private final KitManager kit;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public KitGiveArgument(CelesteKit main) {
        super(false, "give", "dar", "enviar");
        this.main = main;
        this.config = main.getConfigManager();
        this.kit = main.getKitFactory().getKit();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        if (!sender.hasPermission(perm)) {
            chat.send(sender, "No-Permission.Admin");
            return;
        }

        if (args.length != 2) {
            chat.send(sender, "Give.Invalid-Argument");
            bar.send(sender, "Give.Invalid-Argument-Bar");
            title.send(sender, "Give.Invalid-Argument-Title");
            return;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Error.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Error.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Error.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return;
        }

        KitArgument kitArg = kit.getKit(args[1]);
        if (kitArg == null) {
            chat.send(sender, "Error.Kit-Not-Found",
                    chat.build("{name}", args[1]));
            bar.send(sender, "Error.Kit-Not-Found-Bar",
                    chat.build("{name}", args[1]));
            title.send(sender, "Error.Kit-Not-Found-Title",
                    chat.build("{name}", args[1]));
            return;
        }

        new UserEventBuilder()
                .player(t)
                .kit(kitArg)
                .build();

        String name = config.get("Name", kitArg.getFile());
        chat.send(sender,"Give.Success",
                chat.build("{player}", t.getName()),
                chat.build("{name}", name));
        bar.send(sender,"Give.Success-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{name}", name));
        title.send(sender,"Give.Success-Title",
                chat.build("{player}", t.getName()),
                chat.build("{name}", name));
    }
}
