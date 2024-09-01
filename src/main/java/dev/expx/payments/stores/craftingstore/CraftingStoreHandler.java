package dev.expx.payments.stores.craftingstore;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.craftingstore.core.CraftingStore;

public class CraftingStoreHandler {

    private CraftingStore craftingStore;
    public static YamlDocument config;

    public CraftingStoreHandler(YamlDocument config2) {
        config = config2;
    }

    public void enable() {
        this.craftingStore = new CraftingStore(new CSMinestomImpl());
    }

    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    public static YamlDocument getConfig() {
        return config;
    }
}
