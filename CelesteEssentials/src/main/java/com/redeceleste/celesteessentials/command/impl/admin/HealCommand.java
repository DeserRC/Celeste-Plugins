package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public HealCommand(CelesteEssentials main) {
        super("heal", "vida");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                chat.send(sender, "This command cannot be executed via the console");
                return false;
            }

            Player p = (Player) sender;
            String perm = config.getConfig("Permission.Admin");
            String healPerm = config.getConfig("Permission.Heal");
            if (!p.hasPermission(perm) && !p.hasPermission(healPerm)) {
                chat.send(p, "No-Permission.Admin");
                return false;
            }

            p.setHealth(20);
            p.setFireTicks(0);
            p.setFoodLevel(20);
            p.setSaturation(20);
            p.setExhaustion(0);

            chat.send(p, "Heal.Success");
            bar.send(p, "Heal.Success-Bar");
            title.send(p, "Heal.Success-Title");
            return false;
        }

        String perm = config.getConfig("Permission.Admin");
        String healPerm = config.getConfig("Permission.Heal-Other-Players");
        if (!sender.hasPermission(perm) && !sender.hasPermission(healPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "Heal.Invalid-Argument");
            bar.send(sender, "Heal.Invalid-Argument-Bar");
            title.send(sender, "Heal.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Heal.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Heal.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Heal.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        t.setHealth(20);
        t.setFireTicks(0);
        t.setFoodLevel(20);
        t.setSaturation(20);
        t.setExhaustion(0);

        chat.send(sender, "Heal.Success-Other-Player",
                chat.build("{player}", t.getName()));
        bar.send(sender, "Heal.Success-Other-Player-Bar",
                chat.build("{player}", t.getName()));
        title.send(sender, "Heal.Success-Other-Player-Title",
                chat.build("{player}", t.getName()));

        chat.send(t, "Heal.Receive",
                chat.build("{executor}", sender.getName()));
        bar.send(t, "Heal.Receive-Bar",
                chat.build("{executor}", sender.getName()));
        title.send(t, "Heal.Receive-Title",
                chat.build("{executor}", sender.getName()));
        return false;
    }
}
