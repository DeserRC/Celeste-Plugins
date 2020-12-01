package com.redeceleste.celesteessentials.command.impl.admin.args;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;

public class EssentialsReloadArg extends CommandArgument {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public EssentialsReloadArg(CelesteEssentials main) {
        super(false, "reload", "rl", "recarregar");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String reloadPerm = config.getConfig("Permission.Reload");
        if (!sender.hasPermission(perm) && !sender.hasPermission(reloadPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return;
        }

        if (args.length != 0) {
            chat.send(sender, "Reload.Invalid-Argument");
            bar.send(sender, "Reload.Invalid-Argument-Bar");
            title.send(sender, "Reload.Invalid-Argument-Title");
            return;
        }

        config.reload();
        chat.send(sender, "Reload.Success");
        bar.send(sender, "Reload.Success-Bar");
        title.send(sender, "Reload.Success-Title");
    }
}
