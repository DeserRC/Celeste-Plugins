package com.redeceleste.celesteshop.factory;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.util.impl.BarUtil;
import com.redeceleste.celesteshop.util.impl.ChatUtil;
import com.redeceleste.celesteshop.util.impl.TitleUtil;
import lombok.Getter;

@Getter
public class MessageFactory {
    private final Main main;
    private final ChatUtil chat;
    private final TitleUtil title;
    private final BarUtil bar;

    public MessageFactory(Main main) {
        this.main = main;
        this.chat = new ChatUtil(main);
        this.title = new TitleUtil(main);
        this.bar = new BarUtil(main);
    }
}
