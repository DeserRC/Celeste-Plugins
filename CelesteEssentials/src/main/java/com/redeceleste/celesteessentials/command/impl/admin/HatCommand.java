package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static org.bukkit.Material.AIR;

public class HatCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public HatCommand(CelesteEssentials main) {
        super("hat", "chapeu");
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
        String hatPerm = config.getConfig("Permission.Hat");
        if (!p.hasPermission(perm) && !p.hasPermission(hatPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(p, "Hat.Invalid-Argument");
            bar.send(p, "Hat.Invalid-Argument-Bar");
            title.send(p, "Hat.Invalid-Argument-Title");
            return false;
        }

        PlayerInventory inv = p.getInventory();
        ItemStack hand = p.getItemInHand();
        ItemStack helmet = inv.getHelmet();

        if (hand == null && helmet == null || hand.getType().equals(AIR) && helmet.getType().equals(AIR) || hand.getType().equals(AIR) && helmet == null || hand == null && helmet.getType().equals(AIR)) {
            chat.send(p, "Hat.Hat-Not-Found");
            bar.send(p, "Hat.Hat-Not-Found-Bar");
            title.send(p, "Hat.Hat-Not-Found-Title");
            return false;
        }

        inv.setHelmet(hand);
        inv.setItemInHand(helmet);

        chat.send(p, "Hat.Success");
        bar.send(p, "Hat.Success-Bar");
        title.send(p, "Hat.Success-Title");
        return false;
    }
}
