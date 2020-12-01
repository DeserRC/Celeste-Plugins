package com.redeceleste.celesteessentials.type;

import com.redeceleste.celesteessentials.CelesteEssentials;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.GameMode;

@AllArgsConstructor
@Getter
public enum GamemodeType {
    SURVIVAL(CelesteEssentials.getInstance().getConfigManager().getMessage("Gamemode.Survival"), "s", 0, GameMode.SURVIVAL),
    CREATIVE(CelesteEssentials.getInstance().getConfigManager().getMessage("Gamemode.Creative"), "c", 1, GameMode.CREATIVE),
    ADVENTURE(CelesteEssentials.getInstance().getConfigManager().getMessage("Gamemode.Adventure"), "a", 2, GameMode.ADVENTURE),
    SPECTATOR(CelesteEssentials.getInstance().getConfigManager().getMessage("Gamemode.Spectator"), "sp", 3, GameMode.SPECTATOR);

    private final String name;
    private final String prefix;
    private final Integer value;
    private final GameMode gamemode;

    public static GamemodeType get(String mode) {
        for (GamemodeType type : values()) {
            if (type.name().equalsIgnoreCase(mode)) return type;
            if (type.getPrefix().equalsIgnoreCase(mode)) return type;
            if (type.getValue().toString().equals(mode)) return type;
            if (type.getName().equalsIgnoreCase(mode)) return type;
        }
        return null;
    }
}
