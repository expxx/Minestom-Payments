package dev.expx.payments.stores.craftingstore.command.sub;

import dev.expx.payments.stores.craftingstore.CraftingStoreHandler;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

/**
 * Reload command for the CraftingStore
 * store type
 */
public class ReloadSub extends Command {

    /**
     * Command to reload the configuration
     * for CraftingStore
     * @param handler {@link dev.expx.payments.stores.craftingstore.CraftingStoreHandler} Store class instance
     */
    public ReloadSub(CraftingStoreHandler handler) {
        super("reload");

        addConditionalSyntax((s, c) -> s.hasPermission("minestom.store.reload"), (s, c) -> {
            handler.getCraftingStore().reload();
            s.sendMessage(Component.text("CraftingStore is reloading"));
        });
    }

}
