package dev.expx.payments;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.expx.payments.exceptions.ConfigSaveException;
import dev.expx.payments.stores.craftingstore.CraftingStoreHandler;
import dev.expx.payments.stores.tebex.TebexHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class PaymentHandler {

    private YamlDocument config;
    private final Logger logger = LoggerFactory.getLogger(PaymentHandler.class);

    public void init(StoreType type, Path configDir) {
        try {
            try {
                if (!configDir.toFile().isDirectory()) Files.delete(configDir);
                if (!configDir.toFile().exists()) Files.createDirectory(configDir);
            } catch(NoSuchFileException ex) { Files.createDirectory(configDir); }
        } catch(IOException ex) { throw new ConfigSaveException("Unable to save config."); }
        File configFile = new File(configDir.toFile(), "config.yml");

        switch(type) {
            case TEBEX_STORE -> {
                config = createConfig(configFile, getClass().getResourceAsStream("tebex.yml"));
                new TebexHandler(config, configFile).enable();
            }
            case CRAFTINGSTORE_STORE -> {
                config = createConfig(configFile, getClass().getResourceAsStream("craftingstore.yml"));
                new CraftingStoreHandler().enable(config);
            }
            case AGORA_STORE -> throw new IllegalArgumentException("Agora is not currently supported, though is planned.");
            case MINESTORECMS_STORE -> throw new IllegalArgumentException("MineStoreCMS is not currently supported, though is planned.");
        }
    }

    public YamlDocument getConfig() {
        return this.config;
    }

    public Logger getLogger() {
        return logger;
    }

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
