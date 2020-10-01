package com.redeceleste.celesteshop.listener;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.holder.InventoryHolder;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.manager.InventoryManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.model.InventoryType;
import com.redeceleste.celesteshop.util.impl.BarUtil;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import com.redeceleste.celesteshop.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryListener implements Listener {
    private final Main main;
    private final ConfigManager config;
    private final InventoryManager inventory;
    private final ChatUtil chat;
    private final TitleUtil title;
    private final BarUtil bar;

    public InventoryListener(Main main) {
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
        Inventory inv = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();

        if (inv == null) return;
        if (!(e.getInventory().getHolder() instanceof InventoryHolder)) return;

        InventoryType type = ((InventoryHolder) e.getInventory().getHolder()).getType();

        if (type.equals(InventoryType.menu)) {
            HashMap<Integer, FileConfiguration> slot = new HashMap<>();

            for (FileConfiguration file : config.getCategory().getAll()) {
                Integer categorySlot = config.get(file.getName() + ":Categories.Category-Template.Slot", ConfigType.category);
                slot.put(categorySlot, file);
            }

            if (slot.containsKey(e.getSlot())) {
                FileConfiguration file = slot.get(e.getSlot());
                inventory.openInventoryCategory(p, file.getName());
            }
        }

        if (type.equals(InventoryType.category)) {
            String file = ((InventoryHolder) e.getInventory().getHolder()).getFile();

            if (config.get(file + ":Back.Slot", ConfigType.category).equals(e.getSlot())) {
                inventory.openInventory(p);
            }

            for (String menu : config.getKeys(file + ":Categories.ItensBuy", ConfigType.category)) {
                Integer slot = config.get(file + ":Categories.ItensBuy." + menu + ".Slot", ConfigType.category);
                if (slot.equals(e.getSlot())) {
                    inventory.openInventoryConfirm(p, file, menu);
                }
            }
        }

        if (type.equals(InventoryType.confirm)) {
            String path = ((InventoryHolder) e.getInventory().getHolder()).getFile();
            ItemStack is = ((InventoryHolder) e.getInventory().getHolder()).getIs();
            Integer slotConfirm = config.get("InventoryConfirm.Confirm.Slot", ConfigType.config);
            Integer slotReject = config.get("InventoryConfirm.Reject.Slot", ConfigType.config);

            if (slotConfirm.equals(e.getSlot())) {
                if (config.get(path + "Command.Use", ConfigType.category)) {
                    main.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.get(path + "Command.Command", ConfigType.category));
                } else {
                    p.getInventory().addItem(is);
                }

                if (config.get(path + "MessageBuy.MessageBuy.Use", ConfigType.category)) {
                    chat.send(p, path + "MessageBuy.MessageBuy.Message", ConfigType.category);
                }

                title.send(p, path + "MessageBuy.TitleBuy", ConfigType.category);
                bar.send(p, path + "MessageBuy.ActionBarBuy", ConfigType.category);
            }

            if (slotReject.equals(e.getSlot())) {
                String[] file = path.split(":");
                inventory.openInventoryCategory(p, file[0]);
            }
        }

        e.setCancelled(true);
    }
}
