package com.redeceleste.celesteshop.listener;

import com.redeceleste.celesteshop.CelesteSHOP;
import com.redeceleste.celesteshop.holder.InventoryHolder;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.manager.InventoryManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.model.InventoryType;
import com.redeceleste.celesteshop.model.UpdateType;
import com.redeceleste.celesteshop.util.impl.BarUtil;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import com.redeceleste.celesteshop.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private final CelesteSHOP main;
    private final ConfigManager config;
    private final InventoryManager inventory;
    private final ChatUtil chat;
    private final TitleUtil title;
    private final BarUtil bar;

    public InventoryListener(CelesteSHOP main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.inventory = main.getInventoryManager();
        this.chat = main.getMessageFactory().getChat();
        this.title = main.getMessageFactory().getTitle();
        this.bar = main.getMessageFactory().getBar();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!(e.getInventory().getHolder() instanceof InventoryHolder)) return;

        InventoryType type = ((InventoryHolder) e.getInventory().getHolder()).getType();

        if (type.equals(InventoryType.menu)) {
            for (String file : config.getCategories().getKeys()) {
                if (config.getCategory(file + ":Categories.Category-Template.Slot").equals(e.getSlot())) {
                    inventory.openInventoryCategory(p, file);
                    break;
                }
            }
        }

        if (type.equals(InventoryType.category)) {
            String file = ((InventoryHolder) e.getInventory().getHolder()).getFile();

            if (config.getCategory(file + ":Categories.Back.Slot").equals(e.getSlot())) {
                inventory.openInventory(p);
            }

            for (String item : config.getKeys(file + ":Categories.ItemsBuy", ConfigType.category)) {
                if (config.getCategory(file + ":Categories.ItemsBuy." + item + ".Slot").equals(e.getSlot())) {
                    inventory.openInventoryConfirm(p, file + ":Categories.ItemsBuy." + item);
                    break;
                }
            }
        }

        if (type.equals(InventoryType.confirm)) {
            String path = ((InventoryHolder) e.getInventory().getHolder()).getFile();
            ItemStack is = ((InventoryHolder) e.getInventory().getHolder()).getIs();

            if (config.getConfig("InventoryConfirm.Confirm.Slot").equals(e.getSlot())) {
                if (config.getCategory(path + ".Command.Use")) {
                    String command = config.getCategory(path + ".Command.Command");
                    command = command.replace("%player%",  p.getName());
                    main.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                } else {
                    p.getInventory().addItem(is);
                }

                String[] product = path.split("\\.");
                Integer price = config.getCategory("path" + ".Price");
                config.putLog(product[product.length-1], p.getName(), price, UpdateType.buySHOP);

                chat.send(p, path + ".MessageBuy", ConfigType.category);
                title.send(p, path + ".TitleBuy", ConfigType.category);
                bar.send(p, path + ".ActionBarBuy", ConfigType.category);

                p.closeInventory();
            }

            if (config.getConfig("InventoryConfirm.Reject.Slot").equals(e.getSlot())) {
                String[] file = path.split(":");
                inventory.openInventoryCategory(p, file[0]);
            }
        }

        e.setCancelled(true);
    }
}
