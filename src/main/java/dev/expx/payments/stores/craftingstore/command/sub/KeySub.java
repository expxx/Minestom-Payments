package dev.expx.payments.stores.craftingstore.command.sub;

import dev.expx.payments.stores.craftingstore.CraftingStoreHandler;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class KeySub extends Command {

    public KeySub(CraftingStoreHandler handler) {
        super("key");

        ArgumentString key = ArgumentType.String("key");
        addConditionalSyntax((s, c) -> {
            return s.hasPermission("minestom.store.key");
        }, (s, c) -> {
            String value = c.get(key);
            handler.getConfig().set("api-key", value);
            try {
                handler.getConfig().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            new Thread(() -> {
                try {
                    if(handler.getCraftingStore().reload().get())
                        s.sendMessage(Component.text("The new API key is valid. Plugin will now function."));
                    else
                        s.sendMessage(Component.text("The new API key is invalid. The plugin will not work until you set a valid key."));
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }

}
