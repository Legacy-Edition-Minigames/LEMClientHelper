package net.kyrptonaught.lemclienthelper.clientData;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

public class ClientData {

    public static void onInitialize() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (ClientPlayNetworking.canSend(ClientDataNetworking.HAS_MODS_PACKET)) {
                FabricLoader loader = FabricLoader.getInstance();
                boolean hasOptifine = isOptifineLoaded(loader);
                boolean hasControllerMod = isControllerModLoaded(loader);
                ClientDataNetworking.sendHasLEMPacket(sender, hasOptifine, hasControllerMod);
            }
        });
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
