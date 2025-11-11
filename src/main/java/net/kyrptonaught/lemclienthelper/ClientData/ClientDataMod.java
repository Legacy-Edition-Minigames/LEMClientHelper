package net.kyrptonaught.lemclienthelper.ClientData;

import net.fabricmc.loader.api.FabricLoader;

public class ClientDataMod {

    public static void onInitialize() {
        ClientDataNetworking.sendHasLEMPacket();
    }

    public static boolean isOptifineLoaded(FabricLoader loader) {
        return loader.isModLoaded("optifabric") || loader.isModLoaded("optifine");
    }

    public static boolean isL4JLoaded(FabricLoader loader) {
        return loader.isModLoaded("legacy");
    }

    public static boolean isControllerModLoaded(FabricLoader loader) {
        return loader.isModLoaded("midnightcontrols") ||
        //      loader.isModLoaded("legacy") || // Should L4J be counted as a controller mod?
                loader.isModLoaded("lambdacontrols") ||
                loader.isModLoaded("controllable") ||
                loader.isModLoaded("controllermod");
    }
}
