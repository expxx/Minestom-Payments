package dev.expx.payments.stores.craftingstore.command.sub;

import dev.expx.payments.stores.craftingstore.CraftingStoreHandler;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

public class ReloadSub extends Command {

    public ReloadSub(CraftingStoreHandler handler) {
        super("reload");

        addConditionalSyntax((s, c) -> s.hasPermission("minestom.store.reload"), (s, c) -> {
            handler.getCraftingStore().reload();
            s.sendMessage(Component.text("CraftingStore is reloading"));
        });
    }

}
