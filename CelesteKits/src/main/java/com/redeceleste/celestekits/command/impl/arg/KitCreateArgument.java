package com.redeceleste.celestekits.command.impl.arg;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.command.CommandArgument;
import com.redeceleste.celestekits.manager.ConfigManager;
import com.redeceleste.celestekits.manager.KitManager;
import com.redeceleste.celestekits.model.CategoryArgument;
import com.redeceleste.celestekits.util.impl.BarUtil;
import com.redeceleste.celestekits.util.impl.ChatUtil;
import com.redeceleste.celestekits.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

import static com.redeceleste.celestekits.util.DateUtil.formatDate;
import static java.util.concurrent.TimeUnit.SECONDS;

public class KitCreateArgument extends CommandArgument {
    private final CelesteKit main;
    private final ConfigManager config;
    private final KitManager kit;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public KitCreateArgument(CelesteKit main) {
        super(true, "create", "criar");
        this.main = main;
        this.config = main.getConfigManager();
        this.kit = main.getKitFactory().getKit();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        String perm = config.getConfig("Permission.Admin");
        if (!p.hasPermission(perm)) {
            chat.send(p, "No-Permission.Admin");
            return;
        }

        String permission = "";
        if (args.length != 3 || Pattern.compile("[^0-9]").matcher(args[2]).find()) {
            if (args.length != 4 || Pattern.compile("[^0-9]").matcher(args[3]).find()) {
                chat.send(p, "Create.Invalid-Argument");
                bar.send(p, "Create.Invalid-Argument-Bar");
                title.send(p, "Create.Invalid-Argument-Title");
                return;
            } else permission = args[2];
        }

        String category = args[0];
        String name = args[1];
        int delay = Integer.parseInt(args[args.length - 1]);

        CategoryArgument categoryArg = kit.getCategory(category);
        if (categoryArg == null) {
            chat.send(p, "Errorr.Category-Not-Found",
                    chat.build("{name}", args[0]));
            bar.send(p, "Errorr.Category-Not-Found-Bar",
                    chat.build("{name}", args[0]));
            title.send(p, "Errorr.Category-Not-Found-Title",
                    chat.build("{name}", args[0]));
            return;
        }

        ItemStack[] items = p.getInventory().getContents();
        if (items.length == 0) {
            chat.send(p, "Errorr.Not-Have-Items");
            bar.send(p, "Errorr.Not-Have-Items-Bar");
            title.send(p, "Errorr.Not-Have-Items-Title");
            return;
        }

        kit.create(categoryArg, name, permission, delay, items);

        String delayFormatted = formatDate(System.currentTimeMillis() + SECONDS.toMillis(delay));
        chat.send(p, "Create.Success",
                chat.build("{name}", name),
                chat.build("{permission}", permission),
                chat.build("{delay}", delayFormatted));
        bar.send(p, "Create.Success-Bar",
                chat.build("{name}", name),
                chat.build("{permission}", name),
                chat.build("{delay}", delayFormatted));
        title.send(p, "Create.Success-Title",
                chat.build("{name}", name),
                chat.build("{permission}", permission),
                chat.build("{delay}", delayFormatted));
    }
}
