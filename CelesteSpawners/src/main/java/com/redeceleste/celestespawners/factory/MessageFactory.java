package com.redeceleste.celestespawners.factory;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.util.impl.BarUtil;
import com.redeceleste.celestespawners.util.impl.ChatUtil;
import com.redeceleste.celestespawners.util.impl.TitleUtil;
import lombok.Getter;

@Getter
public class MessageFactory {
    private final CelesteSpawners main;
    private final ChatUtil chat;
    private final TitleUtil title;
    private final BarUtil bar;

    public MessageFactory(CelesteSpawners main) {
        this.main = main;
        this.chat = new ChatUtil(main);
        this.title = new TitleUtil(main);
        this.bar = new BarUtil(main);
    }
}