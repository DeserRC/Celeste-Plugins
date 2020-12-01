package com.redeceleste.celesteessentials.listener;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.manager.CommandManager;
import com.redeceleste.celesteessentials.manager.WarpManager;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    private final CelesteEssentials main;
    private final CommandManager command;
    private final WarpManager warp;
    private final ChatUtil chat;

    public CommandListener(CelesteEssentials main) {
        this.main = main;
        this.command = main.getCommandManager();
        this.warp = main.getWarpManager();
        this.chat = main.getMessageFactory().getChat();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandProcess(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String cmd = e.getMessage();

        cmd = cmd.replace("/", "");
        if (cmd.equals("")) return;

        if (cmd.contains(" ")) {
            cmd = cmd.trim().split(" ") [0];
        } else cmd = cmd.trim();

        if (command.commandExist(cmd)) {
            e.setCancelled(true);
            command.send(p, cmd);
        }

        Boolean teleport = warp.teleportPlayer(p, cmd);
        if (teleport == null) return;
        if (teleport) e.setCancelled(true);
        if (!teleport) {
            e.setCancelled(true);
            chat.send(p, "No-Permission.Warp",
                    chat.build("{warp}", cmd.toUpperCase()));
        }
    }
}
