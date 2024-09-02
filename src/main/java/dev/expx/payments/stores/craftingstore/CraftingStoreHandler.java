package dev.expx.payments.stores.craftingstore;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.expx.payments.stores.craftingstore.command.CraftingStoreCommand;
import net.craftingstore.core.CraftingStore;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;

public class CraftingStoreHandler {

    private CraftingStore craftingStore;
    private static YamlDocument yamlDocument;

    public CraftingStoreHandler() {
    }

    public void enable(YamlDocument config) {
        yamlDocument = config;

        this.craftingStore = new CraftingStore(new CSMinestomImpl());

        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new CraftingStoreCommand(this));
    }

    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    public static YamlDocument getConfig() {
        return yamlDocument;
    }
}
