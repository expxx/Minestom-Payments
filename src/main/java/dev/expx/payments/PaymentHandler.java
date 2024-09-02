package dev.expx.payments;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.expx.payments.exceptions.ConfigSaveException;
import dev.expx.payments.exceptions.NotYetImplementedException;
import dev.expx.payments.stores.craftingstore.CraftingStoreHandler;
import dev.expx.payments.stores.tebex.TebexHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Init class for the core platform
 */
public class PaymentHandler {

    /**
     * Config file
     */
    private YamlDocument config;

    /**
     * Logger this class will use
     * @see org.slf4j.Logger
     */
    private final Logger logger = LoggerFactory.getLogger(PaymentHandler.class);

    /**
     * Don't allow regular initialization
     * of this class
     *
     * @throws UnsupportedOperationException Prevents initialization
     */
    public PaymentHandler() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }


    /**
     * Init function to start up
     * the system
     * @param type Type of store to launch (see {@link dev.expx.payments.StoreType})
     * @param configDir Location of where to store the config file
     */
    public void init(StoreType type, Path configDir) {
        try {
            if (!configDir.toFile().isDirectory()) Files.delete(configDir);
            if (!configDir.toFile().exists()) Files.createDirectory(configDir);
        } catch(IOException ex) {
            try {
                Files.createDirectory(configDir);
            } catch(IOException ex2) {
                throw new ConfigSaveException(ex2.getMessage());
            }
        }
        File configFile = new File(configDir.toFile(), "config.yml");

        switch(type) {
            case TEBEX_STORE -> {
                config = createConfig(configFile, getClass().getResourceAsStream("tebex.yml"));
                new TebexHandler().enable(config, Path.of("modules/store").toFile());
            }
            case CRAFTINGSTORE_STORE -> {
                config = createConfig(configFile, getClass().getResourceAsStream("craftingstore.yml"));
                CraftingStoreHandler.enable(config);
            }
            case AGORA_STORE -> throw new NotYetImplementedException("Agora is not currently supported, though is planned.");
            case MINESTORECMS_STORE -> throw new NotYetImplementedException("MineStoreCMS is not currently supported, though is planned.");
        }
    }

    /**
     * Method to retrieve the config, generally
     * used within the stores themselves
     * @return {@link dev.dejvokep.boostedyaml.YamlDocument} The config in a way we can parse
     */
    public YamlDocument getConfig() {
        return this.config;
    }

    /**
     * Method to retrieve the logger, generally
     * used within the stores themselves
     * @return {@link org.slf4j.Logger} The logger we can use
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Private methdo for saving the config
     * without having to catch errors, this
     * method does that for us.
     * @param file Output file, where should the config be referenced from
     * @param stream Input file, if the config doesn't exist, make it off of this
     * @return {@link dev.dejvokep.boostedyaml.YamlDocument} The config in a way we can read
     */
    protected YamlDocument createConfig(File file, InputStream stream) {
        try {
            return YamlDocument.create(
                    file,
                    stream,
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(
                            new BasicVersioning("config-version")
                    ).build()
            );
        } catch(IOException ex) { throw new ConfigSaveException("Unable to save config."); }
    }
}
