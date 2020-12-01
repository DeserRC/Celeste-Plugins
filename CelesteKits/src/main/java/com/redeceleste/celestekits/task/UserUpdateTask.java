package com.redeceleste.celestekits.task;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.dao.UserDAO;
import com.redeceleste.celestekits.factory.UserFactory;
import com.redeceleste.celestekits.model.UserArgument;

public class UserUpdateTask implements Runnable {
    private final CelesteKit main;
    private final UserFactory user;
    private final UserDAO dao;
    private final Boolean async;

    public UserUpdateTask(CelesteKit main, Boolean async) {
        this.main = main;
        this.user = main.getUserFactory();
        this.dao = main.getConnectionFactory().getDao();
        this.async = async;
    }

    @Override
    public void run() {
        for (UserArgument kitArg : user.getCooldown().values()) {
            String player = kitArg.getPlayer();
            if (!user.getUpdate().contains(player.toLowerCase())) continue;
            user.getUpdate().remove(player.toLowerCase());
            dao.replace(kitArg, async);
        }
    }
}
