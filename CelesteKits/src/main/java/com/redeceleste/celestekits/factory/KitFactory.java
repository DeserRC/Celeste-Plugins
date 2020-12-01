package com.redeceleste.celestekits.factory;

import com.redeceleste.celestekits.MockKits;
import com.redeceleste.celestekits.manager.KitManager;
import com.redeceleste.celestekits.model.CategoryArgument;
import com.redeceleste.celestekits.model.KitArgument;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class KitFactory {
    private final MockKits main;
    private final KitManager kit;
    private final Map<Integer, CategoryArgument> categories;
    private final Map<CategoryArgument, Map<Integer, KitArgument>> kits;
    private Inventory mainInv;

    public KitFactory(MockKits main) {
        this.main = main;
        this.kit = new KitManager(main, this);
        this.categories = new HashMap<>();
        this.kits = new HashMap<>();
    }
}
