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

import static org.bukkit.Material.POTION;

public class PotionResetArg extends CommandArgument {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public PotionResetArg(CelesteEssentials main) {
        super(true, "reset", "clear", "resetar", "limpar");
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

        if (args.length != 0) {
            chat.send(p, "Potion.Invalid-Argument");
            bar.send(p, "Potion.Invalid-Argument-Bar");
            title.send(p, "Potion.Invalid-Argument-Title");
            return;
        }

        ItemStack newItem = new ItemBuilder(item)
                .clearPotion().toItemStack();
        p.setItemInHand(newItem);

        String mode = config.getMessage("Potion.Reset");
        chat.send(p, "Potion.Success",
                chat.build("{mode}", mode));
        bar.send(p, "Potion.Success-Bar",
                chat.build("{mode}", mode));
        title.send(p, "Potion.Success-Title",
                chat.build("{mode}", mode));
    }
}
