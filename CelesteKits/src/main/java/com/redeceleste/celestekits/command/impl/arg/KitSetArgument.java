package com.redeceleste.celestekits.command.impl.arg;

import com.redeceleste.celestekits.MockKits;
import com.redeceleste.celestekits.command.CommandArgument;
import com.redeceleste.celestekits.manager.ConfigManager;
import com.redeceleste.celestekits.manager.KitManager;
import com.redeceleste.celestekits.model.CategoryArgument;
import com.redeceleste.celestekits.model.KitArgument;
import com.redeceleste.celestekits.util.impl.BarUtil;
import com.redeceleste.celestekits.util.impl.ChatUtil;
import com.redeceleste.celestekits.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Map;

public class KitSetArgument extends CommandArgument {
    private final MockKits main;
    private final ConfigManager config;
    private final KitManager kit;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public KitSetArgument(MockKits main) {
        super(true, "set", "setar");
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

        if (args.length != 1) {
            chat.send(p, "Set.Invalid-Argument");
            bar.send(p, "Set.Invalid-Argument-Bar");
            title.send(p, "Set.Invalid-Argument-Title");
            return;
        }

        KitArgument kitArg = kit.getKit(args[0]);
        if (kitArg == null) {
            chat.send(p, "error.Kit-Not-Found",
                    chat.build("{name}", args[0]));
            bar.send(p, "error.Kit-Not-Found-Bar",
                    chat.build("{name}", args[0]));
            title.send(p, "error.Kit-Not-Found-Title",
                    chat.build("{name}", args[0]));
            return;
        }

        ItemStack[] items = p.getInventory().getContents();
        if (items.length == 0) {
            chat.send(p, "Set.Not-Have-Items");
            bar.send(p, "Set.Not-Have-Items-Bar");
            title.send(p, "Set.Not-Have-Items-Title");
            return;
        }

        File file = null;
        CategoryArgument categoryArg = null;

        for (File fileCategory : config.getCategories().keySet()) {
            FileConfiguration categoryFile = config.getCategories().get(fileCategory);
            Map<File, FileConfiguration> kits = config.getKits().get(categoryFile);

            for (File fileKit : kits.keySet()) {
                FileConfiguration kitFile = kits.get(fileKit);
                if (!kitArg.getFile().equals(kitFile)) continue;
                file = fileKit;

                String categoryName = fileCategory.getName().replace(".yml", "");
                categoryArg = kit.getCategory(categoryName);
                break;
            }
        }

        String name = config.get("Name", kitArg.getFile());
        kit.set(categoryArg, kitArg, file, items);
        chat.send(p, "Set.Success",
                chat.build("{name}", name));
        bar.send(p, "Set.Success-Bar",
                chat.build("{name}", name));
        title.send(p, "Set.Success-Title",
                chat.build("{name}", name));
    }
}
