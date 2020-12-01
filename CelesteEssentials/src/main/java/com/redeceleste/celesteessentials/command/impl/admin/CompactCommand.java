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

import static com.redeceleste.celesteessentials.type.CompactType.compact;

public class CompactCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public CompactCommand(CelesteEssentials main) {
        super("compact", "compactar");
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
        String compactPerm = config.getConfig("Permission.Compact");
        if (!sender.hasPermission(perm) && !p.hasPermission(compactPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(p, "Compact.Invalid-Argument");
            bar.send(p, "Compact.Invalid-Argument-Bar");
            title.send(p, "Compact.Invalid-Argument-Title");
            return false;
        }

        PlayerInventory inv = p.getInventory();
        ItemStack[] items = inv.getContents();

        int amount = compact(p, inv, items);
        if (amount == 0) {
            chat.send(p, "Compact.No-Items-Compactable");
            bar.send(p, "Compact.No-Items-Compactable-Bar");
            title.send(p, "Compact.No-Items-Compactable-Title");
            return false;
        }

        inv = p.getInventory();
        items = inv.getContents();

        amount += compact(p, inv, items);
        chat.send(p, "Compact.Success",
                chat.build("{amount}", amount));
        bar.send(p, "Compact.Success-Bar",
                chat.build("{amount}", amount));
        title.send(p, "Compact.Success-Title",
                chat.build("{amount}", amount));
        return false;
    }
}
