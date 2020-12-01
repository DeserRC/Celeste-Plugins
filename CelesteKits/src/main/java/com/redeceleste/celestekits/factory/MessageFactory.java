package com.redeceleste.celestekits.factory;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.util.impl.BarUtil;
import com.redeceleste.celestekits.util.impl.ChatUtil;
import com.redeceleste.celestekits.util.impl.TitleUtil;
import lombok.Getter;

@Getter
public class MessageFactory {
    private final CelesteKit main;
    private final ChatUtil chat;
    private final TitleUtil title;
    private final BarUtil bar;

    public MessageFactory(CelesteKit main) {
        this.main = main;
        this.chat = new ChatUtil(main);
        this.title = new TitleUtil(main);
        this.bar = new BarUtil(main);
    }
}