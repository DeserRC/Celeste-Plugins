package com.redeceleste.celestekits.manager;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.dao.UserDAO;
import com.redeceleste.celestekits.factory.UserFactory;
import com.redeceleste.celestekits.model.UserArgument;
import com.redeceleste.celestekits.model.impl.User;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final CelesteKit main;
    private final UserFactory user;
    private final UserDAO dao;

    public UserManager(CelesteKit main, UserFactory user) {
        this.main = main;
        this.user = user;
        this.dao = main.getConnectionFactory().getDao();
    }

    public UserArgument getCooldown(String player) {
        if (!user.getCooldown().containsKey(player.toLowerCase())) return null;
        return user.getCooldown().get(player.toLowerCase());
    }

    public UserArgument addCooldown(String player, String name, long cooldown) {
        user.getUpdate().add(player.toLowerCase());
        if (user.getCooldown().containsKey(player.toLowerCase())) {
            UserArgument kitArg = user.getCooldown().get(player.toLowerCase());
            kitArg.getKit().put(name, cooldown);
            return kitArg;
        }

        Map<String, Long> map = new HashMap<>();
        map.put(name, cooldown);
        UserArgument kitArg = new User(player, map);
        user.getCooldown().put(player.toLowerCase(), kitArg);
        return kitArg;
    }

    public void removeCooldown(String player, String name) {
        user.getUpdate().add(player.toLowerCase());
        if (!user.getCooldown().containsKey(player.toLowerCase())) return;
        UserArgument kitArg = user.getCooldown().get(player.toLowerCase());
        user.getCooldown().get(player.toLowerCase()).getKit().remove(name);
        if (kitArg.getKit().size() == 0) {
            dao.delete(player, true);
        }
    }
}
