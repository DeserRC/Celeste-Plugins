package com.redeceleste.celestespawners.command.impl;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.command.Command;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.task.KillAllTask;
import com.redeceleste.celestespawners.util.impl.ChatUtil;
import org.bukkit.command.CommandSender;

public class KillAllCommand extends Command {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final KillAllTask killAll;
    private final ChatUtil chat;

    public KillAllCommand(CelesteSpawners main) {
        super("killall", "remove", "removeentities", "removeentity");
        this.main = main;
        this.config = main.getConfigManager();
        this.killAll = main.getTaskFactory().getKillAll();
        this.chat = main.getMessageFactory().getChat();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length != 0) {
            setupArguments(sender);
            return false;
        }

        String perm = config.getConfig("Permission.Admin");
        if (!sender.hasPermission(perm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        killAll.run();
        return false;
    }

    private void setupArguments(CommandSender sender) {
        String perm = config.getConfig("Permission.Admin");
        if (sender.hasPermission(perm)) {
            chat.send(sender, "Help.Message-Admin");
            return;
        }
        chat.send(sender, "Help.Message");
    }
}
