package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.builder.ItemBuilder;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.type.EnchantType;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

import static com.redeceleste.celesteessentials.type.EnchantType.getType;
import static org.bukkit.Material.AIR;

public class EnchantCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public EnchantCommand(CelesteEssentials main) {
        super("enchant", "encantar");
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
        String enchantPerm = config.getConfig("Permission.Enchant");
        if (!p.hasPermission(perm) && !p.hasPermission(enchantPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 2 || Pattern.compile("[^0-9]").matcher(args[1]).find() || Integer.parseInt(args[1]) < 0) {
            chat.send(p, "Enchant.Invalid-Argument");
            bar.send(p, "Enchant.Invalid-Argument-Bar");
            title.send(p, "Enchant.Invalid-Argument-Title");
            return false;
        }

        ItemStack item = p.getItemInHand();
        if (item == null || item.getType().equals(AIR)) {
            chat.send(p, "Enchant.Item-Not-Found");
            bar.send(p, "Enchant.Item-Not-Found-Bar");
            title.send(p, "Enchant.Item-Not-Found-Title");
            return false;
        }

        EnchantType enchant = getType(args[0]);
        int level = Integer.parseInt(args[1]);
        if (enchant == null) {
            chat.send(p, "Enchant.Enchant-Not-Found",
                    chat.build("{name}", args[0]));
            bar.send(p, "Enchant.Enchant-Not-Found-Bar",
                    chat.build("{name}", args[0]));
            title.send(p, "Enchant.Enchant-Not-Found-Title",
                    chat.build("{name}", args[0]));
            return false;
        }

        if (level == 0) {
            ItemStack newItemStack = new ItemBuilder(item)
                    .removeEnchantment(enchant.name()).toItemStack();
            p.setItemInHand(newItemStack);
            return false;
        }

        ItemStack newItemStack = new ItemBuilder(item)
                .addEnchant(enchant.name(), level).toItemStack();
        p.setItemInHand(newItemStack);

        chat.send(p, "Enchant.Success",
                chat.build("{name}", args[0]),
                chat.build("{level}", level));
        bar.send(p, "Enchant.Success-Bar",
                chat.build("{name}", args[0]),
                chat.build("{level}", level));
        title.send(p, "Enchant.Success-Title",
                chat.build("{name}", args[0]),
                chat.build("{level}", level));
        return false;
    }
}
