package dev.expx.payments.stores.tebex.command;

import dev.expx.payments.stores.tebex.TebexHandler;
import dev.expx.payments.stores.tebex.command.sub.BanSub;
import dev.expx.payments.stores.tebex.command.sub.ForceSub;
import dev.expx.payments.stores.tebex.command.sub.LookupSub;
import net.minestom.server.command.builder.Command;

/**
 * Main command for the Tebex
 * store platform
 */
public class TebexCommand extends Command {

    /**
     * Handler for the main command
     * of the Tebex store platform
     * @param handler {@link dev.expx.payments.stores.tebex.TebexHandler} Store class instance
     */
    public TebexCommand(TebexHandler handler) {
        super("tebex");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Hey from tebex!"));

        addSubcommand(new BanSub(handler));
        addSubcommand(new ForceSub(handler));
        addSubcommand(new LookupSub(handler));
    }

}
