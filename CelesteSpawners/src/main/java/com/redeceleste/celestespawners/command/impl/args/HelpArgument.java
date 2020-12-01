package com.redeceleste.celestespawners.command.impl.args;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.command.CommandArgument;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.util.impl.ChatUtil;
import org.bukkit.command.CommandSender;

public class HelpArgument extends CommandArgument {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final ChatUtil chat;

    public HelpArgument(CelesteSpawners main) {
        super(false, "help", "ajuda");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        if (sender.hasPermission(perm)) {
            chat.send(sender, "Help.Message-Admin");
            return;
        }
        chat.send(sender, "Help.Message");
    }
}
