package com.redeceleste.celesteessentials.factory;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import lombok.Getter;

@Getter
public class MessageFactory {
    private final CelesteEssentials main;
    private final ChatUtil chat;
    private final TitleUtil title;
    private final BarUtil bar;

    public MessageFactory(CelesteEssentials main) {
        this.main = main;
        this.chat = new ChatUtil(main);
        this.title = new TitleUtil(main);
        this.bar = new BarUtil(main);
    }
}