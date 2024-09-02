package dev.expx.payments.stores.craftingstore;

import dev.expx.payments.OPSender;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.models.donation.Donation;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.util.Objects;

public class CSMinestomImpl implements CraftingStorePlugin {
    @Override
    public boolean executeDonation(Donation donation) {
        if(donation.getPlayer().isRequiredOnline() && !Objects.requireNonNull(MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(donation.getPlayer().getUsername())).isOnline())
            return false;

        MinecraftServer.getCommandManager().execute(new OPSender(), donation.getCommand());
        return true;
    }

    @Override
    public CraftingStoreLogger getLogger() {
        return null;
    }

    @Override
    public void registerRunnable(Runnable runnable, int i, int i1) {
        MinecraftServer.getSchedulerManager().buildTask(runnable).delay(TaskSchedule.tick(i)).repeat(TaskSchedule.tick(i1)).schedule();
    }

    @Override
    public void runAsyncTask(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public String getToken() {
        final String apiKeyLiteral = "api-key";
        String apiKey = CraftingStoreHandler.getConfig().getString(apiKeyLiteral);
        if(apiKey == null || Objects.equals(apiKey, "NEEDS_FILLING_IN")) {
            CraftingStoreHandler.getConfig().set(apiKeyLiteral, "NEEDS_FILLING_IN");
            throw new IllegalArgumentException("API Key for CRAFTINGSTORE is not valid.");
        }
        return CraftingStoreHandler.getConfig().getString(apiKeyLiteral);
    }

    @Override
    public PluginConfiguration getConfiguration() {
        return new CSMinestomConfigImpl();
    }
}
