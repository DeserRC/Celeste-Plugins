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
import org.bukkit.inventory.PlayerInventory;

public class ClearCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public ClearCommand(CelesteEssentials main) {
        super("clear", "limpar");
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
            String clearPerm = config.getConfig("Permission.Clear");
            if (!p.hasPermission(perm) && !p.hasPermission(clearPerm)) {
                chat.send(p, "No-Permission.Admin");
                return false;
            }

            PlayerInventory inv = p.getInventory();
            int amount = inv.getContents().length;

            p.setItemOnCursor(null);
            inv.clear();
            inv.setHelmet(null);
            inv.setChestplate(null);
            inv.setLeggings(null);
            inv.setBoots(null);

            chat.send(p, "Clear.Success",
                    chat.build("{amount}", amount));
            bar.send(p, "Clear.Success-Bar",
                    chat.build("{amount}", amount));
            title.send(p, "Clear.Success-Title",
                    chat.build("{amount}", amount));
            return false;
        }

        String perm = config.getConfig("Permission.Admin");
        String clearPerm = config.getConfig("Permission.Clear-Other-Players");
        if (!sender.hasPermission(perm) && !sender.hasPermission(clearPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "Clear.Invalid-Argument");
            bar.send(sender, "Clear.Invalid-Argument-Bar");
            title.send(sender, "Clear.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Clear.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Clear.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Clear.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        PlayerInventory inv = t.getInventory();
        int amount = inv.getContents().length;

        t.setItemOnCursor(null);
        inv.clear();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);

        chat.send(sender, "Clear.Success-Other-Player",
                chat.build("{player}", t.getName()),
                chat.build("{amount}", amount));
        bar.send(sender, "Clear.Success-Other-Player-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{amount}", amount));
        title.send(sender, "Clear.Success-Other-Player-Title",
                chat.build("{player}", t.getName()),
                chat.build("{amount}", amount));

        chat.send(t, "Clear.Receive",
                chat.build("{executor}", sender.getName()),
                chat.build("{amount}", amount));
        bar.send(t, "Clear.Receive-Bar",
                chat.build("{executor}", sender.getName()),
                chat.build("{amount}", amount));
        title.send(t, "Clear.Receive-Title",
                chat.build("{executor}", sender.getName()),
                chat.build("{amount}", amount));
        return false;
    }
}
