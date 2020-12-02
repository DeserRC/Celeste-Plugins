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
import java.util.regex.Pattern;

import static org.bukkit.Material.AIR;

public class LoreCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public LoreCommand(CelesteEssentials main) {
        super("lore", "relore");
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
        String lorePerm = config.getConfig("Permission.Lore");
        if (!p.hasPermission(perm) && !p.hasPermission(lorePerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length < 2 || Pattern.compile("[^0-9]").matcher(args[0]).find() || args[0].length() > 9 || Integer.parseInt(args[0]) < 0) {
            chat.send(p, "Lore.Invalid-Argument");
            bar.send(p, "Lore.Invalid-Argument-Bar");
            title.send(p, "Lore.Invalid-Argument-Title");
            return false;
        }

        int index = Integer.parseInt(args[0]);
        ItemStack item = p.getItemInHand();
        if (item == null || item.getType().equals(AIR)) {
            chat.send(p, "Lore.Item-Not-Found");
            bar.send(p, "Lore.Item-Not-Found-Bar");
            title.send(p, "Lore.Item-Not-Found-Title");
            return false;
        }

        StringJoiner sj = new StringJoiner(" ");
        for (int i=1; i<args.length; i++) {
            sj.add(args[i].replace("&", "\u00A7"));
        }

        ItemStack newItem = new ItemBuilder(item).replaceLore(sj.toString(), index).toItemStack();
        p.setItemInHand(newItem);

        chat.send(sender, "Lore.Success",
                chat.build("{message}", sj));
        bar.send(sender, "Lore.Success-Bar",
                chat.build("{message}", sj));
        title.send(sender, "Lore.Success-Title",
                chat.build("{message}", sj));
        return false;
    }
}
