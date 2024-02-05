package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.MinecraftClient;

import java.util.HashMap;

public class SyncedKeybindsMod {
    public static final String MOD_ID = "syncedkeybinds";

    public static HashMap<String, SyncedKeybind> syncedKeybindList = new HashMap<>();

    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerGsonFile(MOD_ID, new SyncedKeybindsConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);
        SyncedKeybindsNetworking.registerReceivePacket();
        ClientTickEvents.START_WORLD_TICK.register((world) -> {
            if (MinecraftClient.getInstance().currentScreen == null) {
                syncedKeybindList.values().forEach(syncedKeybind -> {
                    if (syncedKeybind.wasPressed())
                        SyncedKeybindsNetworking.sendKeyPacket(syncedKeybind.ID);
                });
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            syncedKeybindList.values().forEach(syncedKeybind -> {
                ((GameOptionKeyExpander) MinecraftClient.getInstance().options).removeSyncedKeybinds(syncedKeybind.getVanillaBind());

            });
            syncedKeybindList.clear();
        });
    }

    public static void registerNewKeybind(String id, String keybind, String controllerbind) {
        getConfig().keybinds.putIfAbsent(id, new SyncedKeybindsConfig.KeybindConfigItem(keybind, controllerbind));
        SyncedKeybindsConfig.KeybindConfigItem keybindConfigItem = getConfig().keybinds.get(id);
        keybindConfigItem.updateDefaults(keybind, controllerbind);

        SyncedKeybind syncedKeybind = new SyncedKeybind(id, keybindConfigItem);
        syncedKeybindList.put(id, syncedKeybind);

        ((GameOptionKeyExpander) MinecraftClient.getInstance().options).addSyncedKeybinds(syncedKeybind.getVanillaBind());
    }

    public static SyncedKeybindsConfig getConfig() {
        return (SyncedKeybindsConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
