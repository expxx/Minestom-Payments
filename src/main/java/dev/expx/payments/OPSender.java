package dev.expx.payments;

import net.minestom.server.permission.PermissionVerifier;
import org.jetbrains.annotations.NotNull;

/**
 * Class used internally to execute
 * commands through the console,
 * while having full permissions to
 * everything.
 */
public class OPSender extends net.minestom.server.command.ConsoleSender {

    /**
     * Don't allow regular initialization
     * of this class
     *
     * @throws UnsupportedOperationException Prevents initialization
     */
    public OPSender() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Return always true when checking if the ConsoleSender has permissions
     * @param permissionName The name of the permission to check for
     * @param permissionVerifier {@link net.minestom.server.permission.PermissionVerifier} Verifier
     * @return Return true always
     */
    @Override
    public boolean hasPermission(@NotNull String permissionName, PermissionVerifier permissionVerifier) {
        return true;
    }
}
