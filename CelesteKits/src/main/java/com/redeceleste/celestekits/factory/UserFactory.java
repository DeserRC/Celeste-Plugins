package com.redeceleste.celestekits.factory;

import com.redeceleste.celestekits.MockKits;
import com.redeceleste.celestekits.manager.UserManager;
import com.redeceleste.celestekits.model.UserArgument;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class UserFactory {
    private final MockKits main;
    private final UserManager user;
    private final Map<String, UserArgument> cooldown;
    private final Set<String> update;

    public UserFactory(MockKits main) {
        this.main = main;
        this.user = new UserManager(main, this);
        this.cooldown = new HashMap<>();
        this.update = new HashSet<>();
    }
}
