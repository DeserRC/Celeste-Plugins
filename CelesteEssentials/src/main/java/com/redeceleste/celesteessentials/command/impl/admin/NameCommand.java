package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.builder.ItemBuilder;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.StringJoiner;

import static org.bukkit.Material.AIR;

public class NameCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public NameCommand(CelesteEssentials main) {
        super("name", "rename");
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
        String namePerm = config.getConfig("Permission.Name");
        if (!p.hasPermission(perm) && !p.hasPermission(namePerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length == 0) {
            chat.send(p, "Name.Invalid-Argument");
            bar.send(p, "Name.Invalid-Argument-Bar");
            title.send(p, "Name.Invalid-Argument-Title");
            return false;
        }

        ItemStack item = p.getItemInHand();
        if (item == null || item.getType().equals(AIR)) {
            chat.send(p, "Name.Item-Not-Found");
            bar.send(p, "Name.Item-Not-Found-Bar");
            title.send(p, "Name.Item-Not-Found-Title");
            return false;
        }

        StringJoiner sj = new StringJoiner(" ");
        for (int i=0; i<args.length; i++) {
            sj.add(args[i].replace("&", "\u00A7"));
        }

        ItemStack newItem = new ItemBuilder(item).setName(sj.toString()).toItemStack();
        p.setItemInHand(newItem);

        chat.send(sender, "Name.Success",
                chat.build("{message}", sj));
        bar.send(sender, "Name.Success-Bar",
                chat.build("{message}", sj));
        title.send(sender, "Name.Success-Title",
                chat.build("{message}", sj));
        return false;
    }
}
