package com.redeceleste.celesteessentials.command.impl.admin.args;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import org.bukkit.command.CommandSender;

public class EssentialsHelpArg extends CommandArgument {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;

    public EssentialsHelpArg(CelesteEssentials main) {
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
