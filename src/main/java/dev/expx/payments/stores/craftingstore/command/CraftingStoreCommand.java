package dev.expx.payments.stores.craftingstore.command;

import dev.expx.payments.stores.craftingstore.CraftingStoreHandler;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

public class CraftingStoreCommand extends Command {

    public CraftingStoreCommand(CraftingStoreHandler handler) {
        super("craftingstore");
        setDefaultExecutor((s, c) -> s.sendMessage(Component.text("Hello from CraftingStore!")));
    }

}
