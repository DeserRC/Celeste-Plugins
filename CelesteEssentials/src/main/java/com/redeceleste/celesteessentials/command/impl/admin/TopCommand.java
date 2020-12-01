package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public TopCommand(CelesteEssentials main) {
        super("top", "topo");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            chat.send(sender, "This command cannot be executed via the console");
            return false;
        }

        Player p = (Player) sender;
        String perm = config.getConfig("Permission.Admin");
        String topPerm = config.getConfig("Permission.Top");
        if (!p.hasPermission(perm) && !p.hasPermission(topPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(p, "Top.Invalid-Argument");
            bar.send(p, "Top.Invalid-Argument-Bar");
            title.send(p, "Top.Invalid-Argument-Title");
            return false;
        }

        Location loc = p.getLocation();
        double y = loc.getY();
        for (int i=loc.getBlockY(); i<=256; i++) {
            loc.setY(i);
            if (!loc.getBlock().isLiquid() && !loc.getBlock().isEmpty()) y = loc.getY() + 1;
        }

        loc.setY(y);
        p.teleport(loc);

        chat.send(p, "Top.Success");
        bar.send(p, "Top.Success-Bar");
        title.send(p, "Top.Success-Title");
        return false;
    }
}
