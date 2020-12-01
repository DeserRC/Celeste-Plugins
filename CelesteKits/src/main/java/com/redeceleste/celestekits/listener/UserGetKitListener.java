package com.redeceleste.celestekits.listener;

import com.redeceleste.celestekits.MockKits;
import com.redeceleste.celestekits.event.impl.UserGetKitEvent;
import com.redeceleste.celestekits.manager.ConfigManager;
import com.redeceleste.celestekits.manager.InventoryManager;
import com.redeceleste.celestekits.manager.UserManager;
import com.redeceleste.celestekits.model.KitArgument;
import com.redeceleste.celestekits.util.impl.BarUtil;
import com.redeceleste.celestekits.util.impl.ChatUtil;
import com.redeceleste.celestekits.util.impl.TitleUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class UserGetKitListener implements Listener {
    private final MockKits main;
    private final ConfigManager config;
    private final InventoryManager inventory;
    private final UserManager user;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public UserGetKitListener(MockKits main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.inventory = main.getInventoryManager();
        this.user = main.getUserFactory().getUser();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerReceiveKit(UserGetKitEvent e) {
        Player p = e.getPlayer();
        KitArgument kitArg = e.getKit();

        FileConfiguration kitFile = kitArg.getFile();
        String name = config.get("Name", kitFile);

        Map<String, Map<ItemStack, List<String>>> kitItems = kitArg.getItems();
        inventory.sendItemPlayer(p, name, kitItems);
        long kitDelay = kitArg.getDelay();
        String kitName = kitArg.getName();
        String adminPermission = config.getConfig("Permission.Admin");
        if (!p.hasPermission(adminPermission)) {
            user.addCooldown(p.getName(), kitName, System.currentTimeMillis() + kitDelay);
        }

        chat.send(p,"Kit.Success",
                chat.build("{name}", name));
        bar.send(p,"Kit.Success-Bar",
                chat.build("{name}", name));
        title.send(p,"Kit.Success-Title",
                chat.build("{name}", name));
    }

}
