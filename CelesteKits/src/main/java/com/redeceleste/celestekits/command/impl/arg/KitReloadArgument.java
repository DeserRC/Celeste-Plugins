package com.redeceleste.celestekits.command.impl.arg;

import com.redeceleste.celestekits.MockKits;
import com.redeceleste.celestekits.command.CommandArgument;
import com.redeceleste.celestekits.manager.ConfigManager;
import com.redeceleste.celestekits.manager.KitManager;
import com.redeceleste.celestekits.util.impl.BarUtil;
import com.redeceleste.celestekits.util.impl.ChatUtil;
import com.redeceleste.celestekits.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;

public class KitReloadArgument extends CommandArgument {
    private final MockKits main;
    private final ConfigManager config;
    private final KitManager kit;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public KitReloadArgument(MockKits main) {
        super(false, "reload", "rl");
        this.main = main;
        this.config = main.getConfigManager();
        this.kit = main.getKitFactory().getKit();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            chat.send(sender, "Reload.Invalid-Argument");
            bar.send(sender, "Reload.Invalid-Argument-Bar");
            title.send(sender, "Reload.Invalid-Argument-Title");
            return;
        }

        config.load();
        kit.load();

        chat.send(sender,"Reload.Success");
        bar.send(sender,"Reload.Success-Bar");
        title.send(sender,"Reload.Success-Title");
    }
}
