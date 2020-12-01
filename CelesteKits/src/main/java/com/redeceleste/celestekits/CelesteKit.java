package com.redeceleste.celestekits;

import com.redeceleste.celestekits.command.impl.KitCommand;
import com.redeceleste.celestekits.command.impl.arg.KitCreateArgument;
import com.redeceleste.celestekits.command.impl.arg.KitGiveArgument;
import com.redeceleste.celestekits.command.impl.arg.KitReloadArgument;
import com.redeceleste.celestekits.command.impl.arg.KitSetArgument;
import com.redeceleste.celestekits.factory.*;
import com.redeceleste.celestekits.listener.InventoryListener;
import com.redeceleste.celestekits.listener.UserGetKitListener;
import com.redeceleste.celestekits.listener.UserListener;
import com.redeceleste.celestekits.manager.ConfigManager;
import com.redeceleste.celestekits.manager.InventoryManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static org.bukkit.event.HandlerList.unregisterAll;

@Getter
public class CelesteKit extends JavaPlugin {
    @Getter
    private static CelesteKit instance;

    private ScheduledExecutorService scheduled;
    private ConfigManager configManager;
    private InventoryManager inventoryManager;

    private MessageFactory messageFactory;
    private ConnectionFactory connectionFactory;
    private KitFactory kitFactory;
    private UserFactory userFactory;
    private TaskFactory taskFactory;

    @Override
    public void onEnable() {
        instance = this;

        scheduled = newScheduledThreadPool(getRuntime().availableProcessors() * 5);

        configManager = new ConfigManager(this);

        messageFactory = new MessageFactory(this);
        connectionFactory = new ConnectionFactory(this);
        kitFactory = new KitFactory(this);
        userFactory = new UserFactory(this);
        taskFactory = new TaskFactory(this);

        inventoryManager = new InventoryManager(this);

        loadCommands();
        loadListeners();
        loadTasks();
    }

    @Override
    public void onDisable() {
        taskFactory.getUpdateTaskSync().run();
        scheduled.shutdown();
        connectionFactory.getDataBase().closeConnection();
        unregisterAll();
    }

    private void loadCommands() {
        new KitCommand(this).setArguments(
                new KitCreateArgument(this),
                new KitGiveArgument(this),
                new KitReloadArgument(this),
                new KitSetArgument(this));
    }

    private void loadListeners() {
        new InventoryListener(this);
        new UserGetKitListener(this);
        new UserListener(this);
    }

    private void loadTasks() {
        kitFactory.getKit().load();
        scheduled.execute(taskFactory.getGetTask());
    }
}
