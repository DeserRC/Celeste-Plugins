package com.redeceleste.celestekits.listener;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.builder.UserEventBuilder;
import com.redeceleste.celestekits.factory.KitFactory;
import com.redeceleste.celestekits.holder.InventoryCategoryHolder;
import com.redeceleste.celestekits.holder.InventoryConfirmHolder;
import com.redeceleste.celestekits.holder.InventoryMainHolder;
import com.redeceleste.celestekits.holder.InventoryViewerHolder;
import com.redeceleste.celestekits.manager.ConfigManager;
import com.redeceleste.celestekits.manager.InventoryManager;
import com.redeceleste.celestekits.manager.UserManager;
import com.redeceleste.celestekits.model.CategoryArgument;
import com.redeceleste.celestekits.model.KitArgument;
import com.redeceleste.celestekits.model.UserArgument;
import com.redeceleste.celestekits.util.impl.BarUtil;
import com.redeceleste.celestekits.util.impl.ChatUtil;
import com.redeceleste.celestekits.util.impl.TitleUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;

import static com.redeceleste.celestekits.util.DateUtil.formatDate;

public class InventoryListener implements Listener {
    private final CelesteKit main;
    private final KitFactory kit;
    private final ConfigManager config;
    private final InventoryManager inventory;
    private final UserManager user;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public InventoryListener(CelesteKit main) {
        this.main = main;
        this.kit = main.getKitFactory();
        this.config = main.getConfigManager();
        this.inventory = main.getInventoryManager();
        this.user = main.getUserFactory().getUser();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onMainClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!(inv.getHolder() instanceof InventoryMainHolder)) return;

        e.setCancelled(true);

        int slotClick = e.getSlot();
        boolean containsSlot = kit.getCategories().containsKey(slotClick);
        if (!containsSlot) return;

        CategoryArgument categoryArg = kit.getCategories().get(slotClick);
        inventory.openCategory(p, categoryArg);
    }

    @EventHandler
    public void onCategoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!(inv.getHolder() instanceof InventoryCategoryHolder)) return;

        e.setCancelled(true);

        int slotClick = e.getSlot();
        int slotCategory = ((InventoryCategoryHolder) inv.getHolder()).getSlot();

        CategoryArgument categoryArg = kit.getCategories().get(slotCategory);
        FileConfiguration categoryFile = categoryArg.getFile();

        int backSlot = config.get("Custom-Items.Back.Slot", categoryFile);
        if (backSlot == slotClick) inventory.openMain(p);

        Map<Integer, KitArgument> kits = kit.getKits().get(categoryArg);
        boolean containsSlot = kit.getKits().get(categoryArg).containsKey(slotClick);
        if (!containsSlot) return;

        KitArgument kitArg = kits.get(slotClick);
        FileConfiguration kitFile = kitArg.getFile();

        String name = config.get("Name", kitFile);

        String kitName = kitArg.getName();
        String kitPermission = kitArg.getPermission();

        boolean rightClick = e.getClick().isRightClick();
        if (rightClick) {
            inventory.openViewer(p, categoryArg, kitArg);
            return;
        }

        String adminPermission = config.getConfig("Permission.Admin");
        if (!(kitPermission == null)) {
            if (!p.hasPermission(kitPermission) && !p.hasPermission(adminPermission)) {
                chat.send(p, "No-Permission.Kit",
                        chat.build("{name}", name));
                return;
            }
        }

        boolean containsCD = false;
        UserArgument userArg = user.getCooldown(p.getName());
        if (userArg != null) {
            containsCD = userArg.getKit().containsKey(kitName);
        }

        Long delay = containsCD ? userArg.getKit().get(kitName) : null;
        if (delay != null && delay - System.currentTimeMillis() <= 0) {
            containsCD = false;
            user.removeCooldown(p.getName(), kitName);
        }

        if (!containsCD || p.hasPermission(adminPermission)) {
            inventory.openConfirm(p, categoryArg, kitArg);
            return;
        }

        String delayFormatted = formatDate(delay);
        chat.send(p, "Kit.Delay",
                chat.build("{name}", name),
                chat.build("{delay}", delayFormatted));
        bar.send(p, "Kit.Delay-Bar",
                chat.build("{name}", name),
                chat.build("{delay}", delayFormatted));
        title.send(p, "Kit.Delay-Title",
                chat.build("{name}", name),
                chat.build("{delay}", delayFormatted));
    }

    @EventHandler
    public void onViewClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!(inv.getHolder() instanceof InventoryViewerHolder)) return;

        e.setCancelled(true);

        int slotClick = e.getSlot();

        CategoryArgument category = ((InventoryViewerHolder) inv.getHolder()).getCategory();
        int backSlot = config.get("Inventory-View.Custom-Items.Back.Slot", config.getConfig());
        if (backSlot == slotClick) inventory.openCategory(p, category);
    }

    @EventHandler
    public void onConfirmClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!(inv.getHolder() instanceof InventoryConfirmHolder)) return;

        e.setCancelled(true);

        int slotClick = e.getSlot();

        CategoryArgument categoryArg = ((InventoryConfirmHolder) inv.getHolder()).getCategory();
        KitArgument kitArg = ((InventoryConfirmHolder) inv.getHolder()).getKit();

        int denySlot = config.getConfig("Inventory-Confirm.Custom-Items.Deny.Slot");
        int confirmSlot = config.getConfig("Inventory-Confirm.Custom-Items.Confirm.Slot");

        if (denySlot == slotClick) {
            inventory.openCategory(p, categoryArg);
            return;
        }

        if (confirmSlot == slotClick) {
            new UserEventBuilder()
                    .player(p)
                    .kit(kitArg)
                    .build();
            p.closeInventory();
        }
    }
}
