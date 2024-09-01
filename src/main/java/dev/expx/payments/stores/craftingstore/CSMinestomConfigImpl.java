package dev.expx.payments.stores.craftingstore;

import net.craftingstore.core.PluginConfiguration;

public class CSMinestomConfigImpl implements PluginConfiguration {
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
        return "UNKNOWN";
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
