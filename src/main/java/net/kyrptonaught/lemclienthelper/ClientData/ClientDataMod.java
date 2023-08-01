package net.kyrptonaught.lemclienthelper.ClientData;

import net.fabricmc.loader.api.FabricLoader;

public class ClientDataMod {

    public static void onInitialize() {
        ClientDataNetworking.sendHasLEMPacket();
    }

    public static boolean isOptifineLoaded(FabricLoader loader) {
        return loader.isModLoaded("optifabric") || loader.isModLoaded("optifine");
    }

    public static boolean isControllerModLoaded(FabricLoader loader) {
        return loader.isModLoaded("midnightcontrols") ||
                loader.isModLoaded("lambdacontrols") ||
                loader.isModLoaded("controllable") ||
                loader.isModLoaded("controllermod");
    }
}
