package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.resources.ResourceLocation;

public class SyncedKeybindsNetworking {

    @Environment(EnvType.CLIENT)
    public static void sendKeyPacket(ResourceLocation keyID) {
        ClientPlayNetworking.send(new KeybindPressPacket(keyID));
    }

    public static void registerReceivePacket() {
        ClientPlayNetworking.registerGlobalReceiver(SyncKeybindsPacket.PACKET_ID, (payload, context) -> {
            context.client().execute(() -> {
                payload.keybinds().forEach((identifier, keybindConfigItem) -> {
                    SyncedKeybindsMod.registerNewKeybind(identifier, keybindConfigItem.keybinding, keybindConfigItem.controllerBind);
                });
                LEMClientHelperMod.configManager.save(SyncedKeybindsMod.MOD_ID);
            });
        });
    }
}
