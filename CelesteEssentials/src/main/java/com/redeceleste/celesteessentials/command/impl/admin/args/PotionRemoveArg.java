package com.redeceleste.celesteessentials.command.impl.admin.args;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.builder.ItemBuilder;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.regex.Pattern;

import static org.bukkit.Material.POTION;
import static org.bukkit.potion.PotionEffectType.getById;
import static org.bukkit.potion.PotionEffectType.getByName;

public class PotionRemoveArg extends CommandArgument {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public PotionRemoveArg(CelesteEssentials main) {
        super(true, "remove", "remover");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        ItemStack item = p.getItemInHand();
        if (item == null || item.getType() != POTION) {
            chat.send(p, "Potion.Item-Not-Found");
            bar.send(p, "Potion.Item-Not-Found-Bar");
            title.send(p, "Potion.Item-Not-Found-Title");
            return;
        }

        if (args.length != 1) {
            chat.send(p, "Potion.Invalid-Argument");
            bar.send(p, "Potion.Invalid-Argument-Bar");
            title.send(p, "Potion.Invalid-Argument-Title");
            return;
        }

        PotionEffectType type = getByName(args[0]);
        if (type == null) {
            if (Pattern.matches("[0-9]", args[0])) {
                int id = Integer.parseInt(args[0]);
                type = getById(id);
                if (type == null) {
                    chat.send(p, "Potion.Potion-Not-Found",
                            chat.build("{potion}", args[0]));
                    bar.send(p, "Potion.Potion-Not-Found-Bar",
                            chat.build("{potion}", args[0]));
                    title.send(p, "Potion.Potion-Not-Found-Title",
                            chat.build("{potion}", args[0]));
                    return;
                }
            } else {
                chat.send(p, "Potion.Potion-Not-Found",
                        chat.build("{potion}", args[0]));
                bar.send(p, "Potion.Potion-Not-Found-Bar",
                        chat.build("{potion}", args[0]));
                title.send(p, "Potion.Potion-Not-Found-Title",
                        chat.build("{potion}", args[0]));
                return;
            }
        }

        ItemStack newItem = new ItemBuilder(item)
                .removePotion(type.getName())
                .toItemStack();
        p.setItemInHand(newItem);

        String mode = config.getMessage("Potion.Remove");
        chat.send(p, "Potion.Success",
                chat.build("{mode}", mode));
        bar.send(p, "Potion.Success-Bar",
                chat.build("{mode}", mode));
        title.send(p, "Potion.Success-Title",
                chat.build("{mode}", mode));
    }
}
