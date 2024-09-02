package dev.expx.payments.stores.craftingstore;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.expx.payments.stores.craftingstore.command.CraftingStoreCommand;
import net.craftingstore.core.CraftingStore;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;

/**
 * Store handler for the CraftingStore store platform
 */
public final class CraftingStoreHandler {

    private static CraftingStore craftingStore;
    private static YamlDocument yamlDocument;

    /**
     * Don't allow regular initialization
     * of this class
     *
     * @throws UnsupportedOperationException Prevents initialization
     */
    public CraftingStoreHandler() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Run init procedure for the CraftingStore store
     * @param config {@link dev.dejvokep.boostedyaml.YamlDocument} A config we can read
     */
    public static void enable(YamlDocument config) {
        yamlDocument = config;

        craftingStore = new CraftingStore(new CSMinestomImpl());

        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new CraftingStoreCommand(new CraftingStoreHandler()));
    }

    /**
     * Instance of CraftingStore that we can use
     * to interact with it's API
     * @return {@link net.craftingstore.core.CraftingStore} CraftingStore API Instance
     */
    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    /**
     * Get the config in a readable format
     * @return {@link dev.dejvokep.boostedyaml.YamlDocument} Config in readable format
     */
    public static YamlDocument getConfig() {
        return yamlDocument;
    }
}
