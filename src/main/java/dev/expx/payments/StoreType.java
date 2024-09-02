package dev.expx.payments;

/**
 * Type of store the server
 * will be running
 */
public enum StoreType {
    /**
     * Tebex is one of the most used Server
     * Store platforms.
     */
    TEBEX_STORE,

    /**
     * CraftingStore is a lesser known Server
     * Store platform, but still a good option.
     */
    CRAFTINGSTORE_STORE,

    /**
     * Agora is a newly created Server Store
     * platform.
     *
     * @deprecated Not yet implemented
     */
    @Deprecated
    AGORA_STORE,

    /**
     * MineStoreCMS is a self-hosted
     * Server Store platform, created
     * for the nerds who like self-hosting
     * things.
     *
     * @deprecated Not yet implemented
     */
    @Deprecated
    MINESTORECMS_STORE
}
