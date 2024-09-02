package dev.expx.payments.stores.tebex.command.sub;

import dev.expx.payments.stores.tebex.TebexHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class BanSub extends Command {

    public BanSub(TebexHandler handler) {
        super("ban");

        ArgumentString playerArg = ArgumentType.String("player");
        playerArg.setSuggestionCallback((s, c, b) -> {
            for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
                b.addEntry(new SuggestionEntry(player.getUsername()));
        });

        Argument<String> reason = ArgumentType.String("reason").setDefaultValue("None Specified");

        addConditionalSyntax((s, str) -> s.hasPermission("minestom.store.ban"), (s, c) -> {
            if(!handler.isSetup()) throw new RuntimeException("Plugin not setup.");
            try {
                Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(c.get(playerArg));
                if(player == null) throw new ArgumentSyntaxException("Invalid Player", c.get(playerArg), 0);
                InetSocketAddress address = (InetSocketAddress) player.getPlayerConnection().getRemoteAddress();
                boolean success = handler.getSDK().createBan(c.get(playerArg), address.toString(), c.get(reason)).get();
                if(success)
                    s.sendMessage(Component.text(" Player banned from WebStore successfully.", NamedTextColor.GREEN));
                else
                    s.sendMessage(Component.text(" Failed to ban player.", NamedTextColor.RED));
            } catch(InterruptedException | ExecutionException ex) {
                s.sendMessage("An internal error has occurred. Please see console for more information.");
                LoggerFactory.getLogger(BanSub.class).error("An error has occurred while trying to ban a player from the store: {}", ex.getMessage());
            }
        }, playerArg, reason);

    }

}
