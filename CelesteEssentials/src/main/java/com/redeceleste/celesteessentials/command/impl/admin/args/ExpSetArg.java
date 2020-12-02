package com.redeceleste.celesteessentials.command.impl.admin.args;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class ExpSetArg extends CommandArgument {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public ExpSetArg(CelesteEssentials main) {
        super(false, "set", "setar");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player t = Bukkit.getPlayer(args[0]);
        if (args.length != 2 || Pattern.compile("[^0-9]").matcher(args[1]).find() || args[1].length() > 9 || Integer.parseInt(args[1]) < 0) {
            chat.send(sender, "Exp.Invalid-Argument");
            bar.send(sender, "Exp.Invalid-Argument-Bar");
            title.send(sender, "Exp.Invalid-Argument-Title");
            return;
        }

        int exp = Integer.parseInt(args[1]);
        t.giveExpLevels(-t.getLevel());
        t.giveExpLevels(exp);

        String mode = config.getMessage("Exp.Set");
        mode = mode.replace("{player}", t.getName())
                .replace("{level}", String.valueOf(exp));

        chat.send(sender, "Exp.Success",
                chat.build("{player}", t.getName()),
                chat.build("{level}", exp),
                chat.build("{mode}", mode));
        bar.send(sender, "Exp.Success-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{level}", exp),
                chat.build("{mode}", mode));
        title.send(sender, "Exp.Success-Title",
                chat.build("{player}", t.getName()),
                chat.build("{level}", exp),
                chat.build("{mode}", mode));
    }
}
