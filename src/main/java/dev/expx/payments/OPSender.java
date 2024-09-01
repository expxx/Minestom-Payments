package dev.expx.payments;

import net.minestom.server.permission.PermissionVerifier;
import org.jetbrains.annotations.NotNull;

public class OPSender extends net.minestom.server.command.ConsoleSender {
    @Override
    public boolean hasPermission(@NotNull String permissionName, PermissionVerifier permissionVerifier) {
        return true;
    }
}
