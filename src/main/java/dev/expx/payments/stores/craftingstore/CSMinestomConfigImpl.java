package dev.expx.payments.stores.craftingstore;

import net.craftingstore.core.PluginConfiguration;

import static net.minestom.server.MinecraftServer.VERSION_NAME;

/**
 * Config implementation for the CraftingStore store type
 */
public class CSMinestomConfigImpl implements PluginConfiguration {

    /**
     * Don't allow regular initialization
     * of this class
     *
     * @throws UnsupportedOperationException Prevents initialization
     */
    public CSMinestomConfigImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String[] getMainCommands() {
        return new String[]{"craftingstore", "cs"};
    }

    @Override
    public String getVersion() {
        return VERSION_NAME;
    }

    @Override
    public String getPlatform() {
        return "MINESTOM";
    }

    @Override
    public boolean isBuyCommandEnabled() {
        return false;
    }

    @Override
    public int getTimeBetweenCommands() {
        return 200;
    }

    @Override
    public String getNotEnoughBalanceMessage() {
        return "&4You do not have enough in-game money in your account!";
    }

    @Override
    public boolean isUsingAlternativeApi() {
        return false;
    }
}
