package com.deser.listener;

public class InventoryArguments {
    private Integer price;
    private Integer slot;
    private String cmd;
    private String titlebuy;
    private String subtitlebuy;
    private String actionbarmessage;

    public InventoryArguments() {
    }

    public InventoryArguments(String cmd, Integer price, Integer slot, String titlebuy, String subtitlebuy, String actionbarmessage) {
        this.cmd = cmd;
        this.price = price;
        this.slot = slot;
        this.titlebuy = titlebuy;
        this.subtitlebuy = subtitlebuy;
        this.actionbarmessage = actionbarmessage;
    }

    public String getCMD() {
        return cmd;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getSlot() {
        return slot;
    }

    public String getTitlebuy() {
        return titlebuy;
    }

    public String getSubtitlebuy() {
        return subtitlebuy;
    }

    public String getActionbarmessage() {
        return actionbarmessage;
    }
}