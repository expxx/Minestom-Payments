package dev.expx.payments.stores.craftingstore.command.sub;

import dev.expx.payments.exceptions.APIKeyInvalidException;
import dev.expx.payments.exceptions.ConfigSaveException;
import dev.expx.payments.stores.craftingstore.CraftingStoreHandler;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Set secretkey command for the CraftingStore
 * store type
 */
public class KeySub extends Command {

    /**
     * Command to set the secret key
     * in the configuration for CraftingStore
     * @param handler {@link dev.expx.payments.stores.craftingstore.CraftingStoreHandler} Store class instance
     */
    public KeySub(CraftingStoreHandler handler) {
        super("key");

        ArgumentString key = ArgumentType.String("key");
        addConditionalSyntax((s, c) -> s.hasPermission("minestom.store.key"), (s, c) -> {
            String value = c.get(key);
            CraftingStoreHandler.getConfig().set("api-key", value);
            try {
                CraftingStoreHandler.getConfig().save();
            } catch (IOException e) {
                throw new ConfigSaveException(e.getMessage());
            }
            new Thread(() -> {
                try {
                    if(Boolean.TRUE.equals(handler.getCraftingStore().reload().get()))
                        s.sendMessage(Component.text("The new API key is valid. Plugin will now function."));
                    else
                        s.sendMessage(Component.text("The new API key is invalid. The plugin will not work until you set a valid key."));
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                    throw new APIKeyInvalidException(e.getMessage());
                }
            }).start();
        });
    }

}
