package dev.expx.payments;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.expx.payments.stores.tebex.TebexHandler;
import io.tebex.sdk.platform.config.ServerPlatformConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PaymentHandler {

    public static YamlDocument config;
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentHandler.class);

    public void init(StoreType type, Path configDir) {
        try {
            if (!configDir.toFile().isDirectory()) Files.delete(configDir);
            if (!configDir.toFile().exists()) Files.createDirectories(configDir);
        } catch(IOException ex) { ex.printStackTrace(); return; }
        File configFile = new File(configDir.toFile(), "config.yml");
        try {
            if(configFile.isDirectory()) Files.delete(configFile.toPath());
            config = YamlDocument.create(configFile, this.getClass().getResourceAsStream("config.yml"));
        } catch(IOException | NullPointerException ex) { ex.printStackTrace(); return; }

        switch(type) {
            case TEBEX_STORE -> {
                ServerPlatformConfig platformConfig = new ServerPlatformConfig(0);
                platformConfig.setYamlDocument(config);
                new TebexHandler(platformConfig, configFile).enable();
            }
            case AGORA_STORE -> throw new IllegalArgumentException("Agora is not currently supported, though is planned.");
            case CRAFTINGSTORE_STORE -> {

            }
        }
    }

}
