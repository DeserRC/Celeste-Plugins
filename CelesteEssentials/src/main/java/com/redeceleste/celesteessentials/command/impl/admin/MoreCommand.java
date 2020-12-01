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

import static org.bukkit.Material.AIR;

public class MoreCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public MoreCommand(CelesteEssentials main) {
        super("more", "mais");
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
        String morePerm = config.getConfig("Permission.More");
        if (!p.hasPermission(perm) && !p.hasPermission(morePerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(p, "More.Invalid-Argument");
            bar.send(p, "More.Invalid-Argument-Bar");
            title.send(p, "More.Invalid-Argument-Title");
            return false;
        }

        ItemStack item = p.getItemInHand();
        if (item == null || item.getType().equals(AIR)) {
            chat.send(p, "More.Item-Not-Found");
            bar.send(p, "More.Item-Not-Found-Bar");
            title.send(p, "More.Item-Not-Found-Title");
            return false;
        }

        ItemStack newItem = new ItemBuilder(item).setAmount(64).toItemStack();
        p.setItemInHand(newItem);

        chat.send(sender, "More.Success");
        bar.send(sender, "More.Success-Bar");
        title.send(sender, "More.Success-Title");
        return false;
    }
}
