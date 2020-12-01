package com.redeceleste.celestespawners.listener;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.event.impl.PurchaseEvent;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.ItemManager;
import com.redeceleste.celestespawners.util.impl.BarUtil;
import com.redeceleste.celestespawners.util.impl.ChatUtil;
import com.redeceleste.celestespawners.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

import static org.bukkit.Sound.valueOf;

public class PurchaseListener implements Listener {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final ItemManager item;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public PurchaseListener(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.item = main.getSpawnerFactory().getItem();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPurchase(PurchaseEvent e) {
        Player p = e.getPlayer();

        String name = e.getName();
        int amount = e.getAmount();
        int page = e.getPage();
        double price = e.getPrice();

        ItemStack itemStack = e.getItem();
        FileConfiguration itemFile = e.getItemFile();

        double money = main.getEconomy().getBalance(p);

        String formattedPrice = config.formatPrefix(price);

        if (money < price) {
            p.closeInventory();

            Sound sound = valueOf(config.getConfig("Sounds.Purchase-Error"));
            p.playSound(p.getLocation(), sound, 1, 1);
            chat.send(p, "Purchase.Without-Money",
                    chat.build("{money}", formattedPrice));
            bar.send(p, "Purchase.Without-Money-Bar",
                    chat.build("{money}", formattedPrice));
            title.send(p, "Purchase.Without-Money-Title",
                    chat.build("{money}", formattedPrice));
            return;
        }

        boolean useCommand = config.get("Command.Use", itemFile);
        if (useCommand) {
            List<String> commands = config.getList("Command.Commands", itemFile);
            for (String command : commands) {
                command = command
                        .replace("{player}", p.getName())
                        .replace("{amount}", String.valueOf(amount));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        } else {
            int slots = item.getSlots(p.getInventory(), itemStack.getMaxStackSize(), itemStack.getType());
            if (slots < amount) {
                p.closeInventory();

                Sound sound = valueOf(config.getConfig("Sounds.Purchase-Error"));
                p.playSound(p.getLocation(), sound, 1, 1);
                chat.send(p, "Purchase.No-Space");
                bar.send(p, "Purchase.No-Space-Bar");
                title.send(p, "Purchase.No-Space-Title");
                return;
            }

            for (int i=amount; i>0; i--, p.getInventory().addItem(itemStack)) { }
        }

        main.getEconomy().withdrawPlayer(p, price);
        p.closeInventory();

        Sound sound = valueOf(config.getConfig("Sounds.Purchased-Item"));
        p.playSound(p.getLocation(), sound, 1, 1);
        chat.send(p, "Messages.Chat", itemFile,
                chat.build("{player}", p.getName()),
                chat.build("{money}", formattedPrice),
                chat.build("{name}", name),
                chat.build("{amount}", amount),
                chat.build("{page}", page));
        bar.send(p, "Messages.Bar", itemFile,
                chat.build("{player}", p.getName()),
                chat.build("{money}", formattedPrice),
                chat.build("{name}", name),
                chat.build("{amount}", amount),
                chat.build("{page}", page));
        title.send(p, "Messages.Title", itemFile,
                chat.build("{player}", p.getName()),
                chat.build("{money}", formattedPrice),
                chat.build("{name}", name),
                chat.build("{amount}", amount),
                chat.build("{page}", page));

        try {
            config.putLogPurchase("[PURCHASE] " + p.getName() + " bought " + amount + " " + name + " the price of $" + formattedPrice);
        } catch (IOException ex) {
            System.err.printf("Ocorreu um erro ao salvar as de compra do jogador %s: %s", p.getName(), ex.getMessage());
        }
    }
}
