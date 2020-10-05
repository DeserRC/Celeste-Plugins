package com.redeceleste.celesteshop;

import com.redeceleste.celesteshop.command.impl.DrawPoints;
import com.redeceleste.celesteshop.command.impl.PointsCommand;
import com.redeceleste.celesteshop.command.impl.args.HelpCommandArgument;
import com.redeceleste.celesteshop.command.impl.args.PayCommandArgument;
import com.redeceleste.celesteshop.command.impl.ShopCommand;
import com.redeceleste.celesteshop.command.impl.args.admin.*;
import com.redeceleste.celesteshop.factory.ConnectionFactory;
import com.redeceleste.celesteshop.factory.MessageFactory;
import com.redeceleste.celesteshop.factory.PointsFactory;
import com.redeceleste.celesteshop.factory.TaskFactory;
import com.redeceleste.celesteshop.listener.InventoryListener;
import com.redeceleste.celesteshop.listener.PointsListener;
import com.redeceleste.celesteshop.listener.UserListener;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.manager.InventoryManager;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    private ScheduledExecutorService scheduled;
    private ConfigManager configManager;
    private InventoryManager inventoryManager;

    private ConnectionFactory connectionFactory;
    private MessageFactory messageFactory;
    private PointsFactory pointsFactory;
    private TaskFactory taskFactory;

    @Override
    public void onEnable() {
        instance = this;

        scheduled = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()*5);

        configManager = new ConfigManager(this);

        connectionFactory = new ConnectionFactory(this);
        messageFactory = new MessageFactory(this);
        pointsFactory = new PointsFactory(this);
        taskFactory = new TaskFactory(this);

        inventoryManager = new InventoryManager(this);

        scheduled.execute(taskFactory.getGetTask());

        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {
        taskFactory.getUpdateTaskSync().run();
        scheduled.shutdown();
        connectionFactory.getDataBase().closeConnection();
        HandlerList.unregisterAll();
    }

    public void loadCommands() {
        new PointsCommand(this).setArguments(
                new PayCommandArgument(this),
                new AddCommandArgument(this),
                new RemoveCommandArgument(this),
                new SetCommandArgument(this),
                new ResetCommandArgument(this),
                new ReloadCommandArgument(this),
                new HelpCommandArgument(this));
        new ShopCommand(this);
        new DrawPoints(this);
    }

    public void loadListeners() {
        new UserListener(this);
        new PointsListener(this);
        new InventoryListener(this);
    }
}
