package dev.expx.payments.stores.tebex.command.sub;

import dev.expx.payments.exceptions.NotSetupException;
import dev.expx.payments.stores.tebex.TebexHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;

/**
 * Command to forcecheck for new purchases
 * for the Tebex store type
 */
public class ForceSub extends Command {

    /**
     * Command to forcecheck for new payments
     * for the Tebex store type
     * @param handler {@link dev.expx.payments.stores.tebex.TebexHandler} Store class instance
     */
    public ForceSub(TebexHandler handler) {
        super("forcecheck");

        addConditionalSyntax((s, c) -> s.hasPermission("minestom.store.force"), (s, c) -> {
            if(!handler.isSetup()) throw new NotSetupException("Plugin not setup.");
            s.sendMessage(Component.text("Running forcecheck. Please wait.", NamedTextColor.GREEN));
            handler.performCheck(true);
        });
    }

}
