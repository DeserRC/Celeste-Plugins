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

import static org.bukkit.Material.AIR;

public class RepairCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public RepairCommand(CelesteEssentials main) {
        super("repair", "reparar");
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
        String repairPerm = config.getConfig("Permission.Repair");
        if (!p.hasPermission(perm) && !p.hasPermission(repairPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(p, "Repair.Invalid-Argument");
            bar.send(p, "Repair.Invalid-Argument-Bar");
            title.send(p, "Repair.Invalid-Argument-Title");
            return false;
        }

        ItemStack item = p.getItemInHand();
        if (item == null || item.getType().equals(AIR)) {
            chat.send(p, "Repair.Item-Not-Found");
            bar.send(p, "Repair.Item-Not-Found-Bar");
            title.send(p, "Repair.Item-Not-Found-Title");
            return false;
        }

        if (item.getDurability() == 0) {
            chat.send(p, "Repair.Item-Not-Repairable");
            bar.send(p, "Repair.Item-Not-Repairable-Bar");
            title.send(p, "Repair.Item-Not-Repairable-Title");
            return false;
        }

        item.setDurability((short) 0);
        p.setItemInHand(item);

        chat.send(sender, "Repair.Success");
        bar.send(sender, "Repair.Success-Bar");
        title.send(sender, "Repair.Success-Title");
        return false;
    }
}
