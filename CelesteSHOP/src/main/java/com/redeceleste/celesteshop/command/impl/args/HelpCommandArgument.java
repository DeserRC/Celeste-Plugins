package com.redeceleste.celesteshop.command.impl.args;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.command.CommandArgument;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import org.bukkit.command.CommandSender;

public class HelpCommandArgument extends CommandArgument {
    private final Main main;
    private final ConfigManager config;
    private final ChatUtil chat;

    public HelpCommandArgument(Main main) {
        super(false, "help", "ajuda");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(config.getConfig("Admin_Permission").toString())) {
            chat.send(sender, "Message.HelpAdmin");
            return;
        }

        chat.send(sender, "Message.Help");
    }
}
