package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class SyncedKeybinds {
    public static final String MOD_ID = "syncedkeybinds";

    public static List<SyncedKeybind> syncedKeybindList = new ArrayList<>();

    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new SyncedKeybindsConfig());
        SyncedKeybindsNetworking.registerReceivePacket();
        ClientTickEvents.START_WORLD_TICK.register((world) -> {
            if (SyncedKeybindsNetworking.canSendPacket() && MinecraftClient.getInstance().currentScreen == null) {
                syncedKeybindList.forEach(syncedKeybind -> {
                    if (syncedKeybind.keyBinding.wasPressed())
                        SyncedKeybindsNetworking.sendKeyPacket(syncedKeybind.ID);
                });
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            for (int i = syncedKeybindList.size() - 1; i >= 0; i--) {
                ((GameOptionKeyExpander) MinecraftClient.getInstance().options).removeSyncedKeybinds(syncedKeybindList.remove(i).getVanillaBind());
            }
        });
    }

    public static void registerNewKeybind(String id, String keybind, String controllerbind) {
        getConfig().keybinds.putIfAbsent(id, new SyncedKeybindsConfig.KeybindConfigItem(keybind, controllerbind));
        SyncedKeybindsConfig.KeybindConfigItem keybindConfigItem = getConfig().keybinds.get(id);
        SyncedKeybind syncedKeybind = new SyncedKeybind(id, keybindConfigItem.keybinding, keybindConfigItem.controllerBind, keybind);
        syncedKeybindList.add(syncedKeybind);

        ((GameOptionKeyExpander) MinecraftClient.getInstance().options).addSyncedKeybinds(syncedKeybind.getVanillaBind());
    }

    public static SyncedKeybindsConfig getConfig() {
        return (SyncedKeybindsConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
