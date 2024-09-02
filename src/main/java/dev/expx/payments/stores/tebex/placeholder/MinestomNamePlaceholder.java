package dev.expx.payments.stores.tebex.placeholder;

import dev.expx.payments.stores.tebex.TebexHandler;
import io.tebex.sdk.obj.QueuedPlayer;
import io.tebex.sdk.placeholder.Placeholder;
import io.tebex.sdk.placeholder.PlaceholderManager;
import io.tebex.sdk.util.UUIDUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.utils.mojang.MojangUtils;

import java.io.IOException;

/**
 * Used for the Tebex store platform
 * to replace the {username} plaecholder
 * in commands (I think)
 */
public class MinestomNamePlaceholder implements Placeholder {

    private final PlaceholderManager manager;

    /**
     * The initializer
     * @param manager {@link io.tebex.sdk.placeholder.PlaceholderManager} The manager for placeholders
     */
    public MinestomNamePlaceholder(PlaceholderManager manager) {
        this.manager = manager;
    }


    @Override
    public String handle(QueuedPlayer player, String command) {
        if (player.getUuid() == null || player.getUuid().isEmpty()) {
            return manager.getUsernameRegex().matcher(command).replaceAll(player.getName());
        }

        String offlinePlayer = null;
        try {
            offlinePlayer = MojangUtils.getUsername(UUIDUtil.mojangIdToJavaId(player.getUuid()));
        } catch (IOException e) {
            TebexHandler.LOGGER.error("Error during player username fetching",e);
            MinecraftServer.getExceptionManager().handleException(e);
        }
        if (offlinePlayer != null) {
            return manager.getUsernameRegex().matcher(command).replaceAll(offlinePlayer);
        }
        else {
            return manager.getUsernameRegex().matcher(command).replaceAll(player.getName());
        }
    }

}
