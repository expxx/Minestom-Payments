package dev.expx.payments.stores.craftingstore.command;

import dev.expx.payments.stores.craftingstore.CraftingStoreHandler;
import dev.expx.payments.stores.craftingstore.command.sub.KeySub;
import dev.expx.payments.stores.craftingstore.command.sub.ReloadSub;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

/**
 * Main command for the CraftingStore
 * store platform
 */
public class CraftingStoreCommand extends Command {

    /**
     * Handler for the main command
     * of the CraftingStore store platform
     * @param handler {@link dev.expx.payments.stores.craftingstore.CraftingStoreHandler} Store class instance
     */
    public CraftingStoreCommand(CraftingStoreHandler handler) {
        super("craftingstore");
        setDefaultExecutor((s, c) -> s.sendMessage(Component.text("Hello from CraftingStore!")));

        addSubcommand(new KeySub(handler));
        addSubcommand(new ReloadSub(handler));
    }

}
