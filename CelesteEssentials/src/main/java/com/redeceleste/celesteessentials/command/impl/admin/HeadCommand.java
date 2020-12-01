package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.builder.ItemBuilder;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.SKULL_ITEM;

public class HeadCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public HeadCommand(CelesteEssentials main) {
        super("head", "skull", "cabeca", "cabeça", "cb");
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
        String headPerm = config.getConfig("Permission.head");
        if (!p.hasPermission(perm) && !p.hasPermission(headPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1) {
            chat.send(p, "Head.Invalid-Argument");
            bar.send(p, "Head.Invalid-Argument-Bar");
            title.send(p, "Head.Invalid-Argument-Title");
            return false;
        }

        String name = args[0];
        Player t = Bukkit.getPlayer(name);
        if (t != null) name = t.getName();

        ItemStack head = new ItemBuilder(SKULL_ITEM, 1, 3)
                .setName("§c" + name)
                .setSkullOwner(name)
                .toItemStack();

        p.getInventory().addItem(head);
        chat.send(p, "Head.Success",
                chat.build("{player}", name));
        bar.send(p, "Head.Success-Bar",
                chat.build("{player}", name));
        title.send(p, "Head.Success-Title",
                chat.build("{player}", name));
        return false;
    }
}
