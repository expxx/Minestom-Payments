package dev.expx.payments.stores.tebex.command.sub;

import dev.expx.payments.exceptions.NotSetupException;
import dev.expx.payments.stores.tebex.TebexHandler;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;

public class LookupSub extends Command {

    public LookupSub(TebexHandler handler) {
        super("lookup");

        Argument<String> username = ArgumentType.String("username")
                .setSuggestionCallback((s, c, b) -> {
                    for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
                        b.addEntry(new SuggestionEntry(player.getUsername()));
                });
        addConditionalSyntax((s, c) -> s.hasPermission("minestom.store.lookup"), (s, c) -> {
            if(!handler.isSetup()) throw new NotSetupException("Plugin not setup.");
            String user = c.get(username);
            handler.getSDK().getPlayerLookupInfo(user).exceptionally(throwable -> {
                s.sendMessage(Component.text("No information for that player could be found."));
                return null;
            }).thenAccept(info -> {
                if(info == null)
                    s.sendMessage(Component.text("No information for that player could be found."));
                else {
                    s.sendMessage(Component.text("Username: " + info.getLookupPlayer().getUsername()));
                    s.sendMessage(Component.text("ID: " + info.getLookupPlayer().getId()));
                    s.sendMessage(Component.text("Chargeback Rate: " + info.chargebackRate));
                    s.sendMessage(Component.text("Bans Total: " + info.banCount));
                    s.sendMessage(Component.text("Payments: " + info.payments.size()));
                }
            });
        }, username);
    }

}
