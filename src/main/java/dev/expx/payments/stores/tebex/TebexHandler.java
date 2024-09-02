package dev.expx.payments.stores.tebex;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.expx.payments.exceptions.ConfigSaveException;
import dev.expx.payments.stores.tebex.command.TebexCommand;
import dev.expx.payments.stores.tebex.placeholder.MinestomNamePlaceholder;

import io.tebex.sdk.SDK;
import io.tebex.sdk.Tebex;
import io.tebex.sdk.obj.Category;
import io.tebex.sdk.obj.ServerEvent;
import io.tebex.sdk.placeholder.PlaceholderManager;
import io.tebex.sdk.placeholder.defaults.UuidPlaceholder;
import io.tebex.sdk.platform.Platform;
import io.tebex.sdk.platform.PlatformTelemetry;
import io.tebex.sdk.platform.PlatformType;
import io.tebex.sdk.platform.config.IPlatformConfig;
import io.tebex.sdk.platform.config.ServerPlatformConfig;
import io.tebex.sdk.request.response.ServerInformation;
import io.tebex.sdk.util.CommandResult;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.Player;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minestom.server.MinecraftServer.VERSION_NAME;

/**
 * Store handler for the Tebex store platform
 */
public class TebexHandler implements Platform {

    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(4);

    /**
     * The logger, used for logging things
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(TebexHandler.class);

    private SDK sdk;
    private ServerPlatformConfig config;
    private File dataFolder;
    private boolean setup;
    private PlaceholderManager placeholderManager;
    private Map<Object, Integer> queuedPlayers;

    private ServerInformation storeInformation;
    private List<Category> storeCategories;
    private List<ServerEvent> serverEvents;

    /**
     * Run init procedure for the Tebex store
     * @param passedDoc {@link dev.dejvokep.boostedyaml.YamlDocument} A config we can read
     * @param passedFolder The location to store everything related to the store
     */
    public void enable(@NotNull YamlDocument passedDoc, @Nullable File passedFolder) {
        Tebex.init(this);

        config = platformConfig(passedDoc);
        dataFolder = passedFolder;

        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new TebexCommand(this));

        this.sdk = new SDK(this, config.getSecretKey());

        placeholderManager = new PlaceholderManager();
        placeholderManager.register(new UuidPlaceholder(placeholderManager));
        placeholderManager.register(new MinestomNamePlaceholder(placeholderManager));

        queuedPlayers = new ConcurrentHashMap<>();
        storeCategories = new ArrayList<>();
        serverEvents = new ArrayList<>();

        init();
        EXECUTOR.scheduleAtFixedRate(this::refreshListings, 0, 5, TimeUnit.MINUTES);
        EXECUTOR.scheduleAtFixedRate(() -> this.sdk.sendPluginEvents(), 0, 10, TimeUnit.MINUTES);
        EXECUTOR.scheduleAtFixedRate(() -> {
            List<ServerEvent> runEvents = new ArrayList<>(serverEvents.subList(0, Math.min(serverEvents.size(), 750)));
            if (runEvents.isEmpty()) return;
            if (!this.isSetup()) return;

            sdk.sendJoinEvents(runEvents)
                    .thenAccept(aVoid -> {
                        serverEvents.removeAll(runEvents);
                        debug("Successfully sent join events.");
                    })
                    .exceptionally(throwable -> {
                        debug("Failed to send join events: " + throwable.getMessage());
                        return null;
                    });
        }, 0, 1, TimeUnit.MINUTES);
        this.setup = true;
    }

    /**
     * Returns all known information
     * about the store
     * @return {@link io.tebex.sdk.request.response.ServerInformation} Server Information
     */
    public ServerInformation getStoreInformation() {
        return storeInformation;
    }

    /**
     * Returns a list of categories
     * that're meant to be displayed
     * on the store.
     * @return {@link java.util.List<io.tebex.sdk.obj.Category>} List of categories
     */
    public List<Category> getStoreCategories() {
        return storeCategories;
    }

    /**
     * Returns a list of events that are
     * pending to run
     * @return {@link java.util.List<io.tebex.sdk.obj.ServerEvent>} List of server events
     */
    public List<ServerEvent> getServerEvents() {
        return serverEvents;
    }

    /**
     * Used to get the folder that contains
     * all information related to the store
     * @return {@link java.io.File} File of datafolder
     */
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public PlatformType getType() {
        return PlatformType.BUKKIT;
    }

    @Override
    public String getStoreType() {
        return storeInformation == null ? "" : storeInformation.getStore().getGameType();
    }

    @Override    public SDK getSDK() {
        return sdk;
    }

    @Override
    public File getDirectory() {
        return null;
    }

    @Override
    public boolean isSetup() {
        return setup;
    }

    /**
     * Unused
     * @param setup Whether plugin is setup or not
     */
    @Override
    public void setSetup(boolean setup) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOnlineMode() {
        return MojangAuth.isEnabled() && !config.isProxyMode();
    }

    @Override
    public void configure() {
        setup = true;
        performCheck();
        sdk.sendTelemetry();
    }

    @Override
    public void halt() {
        setup = false;
    }

    @Override
    public PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }

    @Override
    public Map<Object, Integer> getQueuedPlayers() {
        return queuedPlayers;
    }

    @Override
    public CommandResult dispatchCommand(String command) {
        var result = MinecraftServer.getCommandManager().execute(MinecraftServer.getCommandManager().getConsoleSender(), command);
        if (result.getType() == net.minestom.server.command.builder.CommandResult.Type.SUCCESS) {
            return CommandResult.from(true);
        }
        else {
            return CommandResult.from(false).withMessage("Invalid input: " + result.getInput());
        }
    }

    @Override
    public void executeAsync(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    @Override
    public void executeAsyncLater(Runnable runnable, long time, TimeUnit unit) {
        EXECUTOR.schedule(runnable, time, unit);
    }

    @Override
    public void executeBlocking(Runnable runnable) {
        MinecraftServer.getSchedulerManager().execute(runnable);
    }

    @Override
    public void executeBlockingLater(Runnable runnable, long time, TimeUnit unit) {
        MinecraftServer.getSchedulerManager().buildTask(runnable).delay(time, unit.toChronoUnit()).schedule();
    }

    @Override
    public boolean isPlayerOnline(Object player) {
        return getPlayer(player) != null;
    }

    @Override
    public int getFreeSlots(Object playerId) {
        var player = getPlayer(playerId);
        if (player == null) return -1;
        ItemStack[] inv = player.getInventory().getItemStacks();
        inv = Arrays.copyOfRange(inv, 0, 36);
        return (int) Arrays.stream(inv).filter(item -> item == null || item.isAir()).count();
    }

    @Override
    public String getVersion() {
        return VERSION_NAME;
    }

    @Override
    public void log(Level level, String message) {
        if (level == Level.INFO) {
            LOGGER.info(message);
        } else if (level == Level.WARNING) {
            LOGGER.warn(message);
        } else if (level == Level.SEVERE) {
            LOGGER.error(message);
        } else {
            LOGGER.debug(message);
        }
    }

    @Override
    public void setStoreInfo(ServerInformation info) {
        storeInformation = info;
    }

    /**
     * Gets a player from an object
     * @param player {@link java.lang.Object} Object version of player
     * @return {@link net.minestom.server.entity.Player} Player
     */
    public Player getPlayer(Object player) {
        if (player == null) return null;
        if (isOnlineMode() && !isGeyser() && player instanceof UUID uuid) {
            return MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        }
        return MinecraftServer.getConnectionManager().getOnlinePlayerByUsername((String) player);
    }

    @Override
    public void setStoreCategories(List<Category> categories) {
        storeCategories = categories;
    }

    @Override
    public IPlatformConfig getPlatformConfig() {
        return config;
    }

    @Override
    public PlatformTelemetry getTelemetry() {
        String serverVersion = VERSION_NAME;

        Pattern pattern = Pattern.compile("MC: (\\d+\\.\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(serverVersion);
        if (matcher.find()) {
            serverVersion = matcher.group(1);
        }

        return new PlatformTelemetry(
                getVersion(),
                MinecraftServer.getBrandName(),
                serverVersion,
                System.getProperty("java.version"),
                System.getProperty("os.arch"),
                MojangAuth.isEnabled()
        );
    }

    @Override
    public String getServerIp() {
        var p = MinecraftServer.process().server();
        return p.getAddress() + ":" + p.getPort();
    }


    /**
     * Create a platform config from a {@link dev.dejvokep.boostedyaml.YamlDocument}
     * @param config {@link dev.dejvokep.boostedyaml.YamlDocument} Config Document
     * @return {@link io.tebex.sdk.platform.config.ServerPlatformConfig} Server Platform Config
     */
    protected ServerPlatformConfig platformConfig(YamlDocument config) {
        ServerPlatformConfig serverPlatformConfig = new ServerPlatformConfig(config.getInt("config-version"));
        try {
            serverPlatformConfig.setYamlDocument(config);

            final String secretKeyPath = "store.secret-key";
            if (config.getString(secretKeyPath) == null) config.set(secretKeyPath, "");
            serverPlatformConfig.setSecretKey(config.getString(secretKeyPath));

            serverPlatformConfig.setBuyCommandEnabled(false);
            serverPlatformConfig.setBuyCommandName("tbxbuy");

            final String updateCheckerPath = "extra.update-checker";
            if (config.getString(updateCheckerPath) == null) config.set(updateCheckerPath, false);
            serverPlatformConfig.setCheckForUpdates(config.getBoolean(updateCheckerPath));

            final String debugPath = "extra.debug";
            if (config.getString(debugPath) == null) config.set(debugPath, false);
            serverPlatformConfig.setVerbose(config.getBoolean(debugPath));

            final String autoReportPath = "extra.auto-report";
            if (config.getString(autoReportPath) == null) config.set(autoReportPath, false);
            serverPlatformConfig.setAutoReportEnabled(config.getBoolean(autoReportPath));

            config.save();
        } catch(IOException ex) { throw new ConfigSaveException("Unable to save config: " + ex.getMessage()); }
        return serverPlatformConfig;
    }


}
