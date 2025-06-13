package net.kyrptonaught.lemclienthelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.kyrptonaught.lemclienthelper.ClientData.ClientDataMod;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloaderMod;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsMod;
import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvMod;
import net.kyrptonaught.lemclienthelper.SpectateSqueaker.SpectateSqueakerMod;
import net.kyrptonaught.lemclienthelper.TakeEverything.TakeEverythingMod;
import net.kyrptonaught.lemclienthelper.customWorldBorder.CustomWorldBorderMod;
import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.kyrptonaught.lemclienthelper.syncedKeybinds.SyncedKeybindsMod;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;

public class LEMClientHelperMod implements ClientModInitializer {
    public static final String MOD_ID = "lemclienthelper";
    public static ConfigManager.MultiConfigManager configManager = new ConfigManager.MultiConfigManager(MOD_ID);


    @Override
    public void onInitializeClient() {
        TakeEverythingMod.onInitialize();
        ResourcePreloaderMod.onInitialize();
        SmallInvMod.onInitialize();
        ClientDataMod.onInitialize();
        ServerInfoMod.onInitialize();
        SyncedKeybindsMod.onInitialize();
        SpectateSqueakerMod.onInitialize();
        ServerConfigsMod.onInitialize();
        CustomWorldBorderMod.onInitialize();
        HudMod.onInitialize();
        // if (FabricLoader.getInstance().isModLoaded("lambdacontrols"))
        if (FabricLoader.getInstance().isModLoaded("midnightcontrols"))
            registerControllerKeys();
        //TODO: Implement this armor hud with config and or server integration
        //configManager.load();
    }

    public static void registerControllerKeys() {
        TakeEverythingMod.registerControllerKeys();
    }

    public static boolean isKeybindPressed(KeyMapping keyBinding, int pressedKeyCode, boolean isMouse) {
        InputConstants.Key keycode = KeyBindingHelper.getBoundKeyOf(keyBinding);

        if (isMouse) {
            if (keycode.getType() != InputConstants.Type.MOUSE) return false;
        } else {
            if (keycode.getType() != InputConstants.Type.KEYSYM) return false;
        }
        return keycode.getValue() == pressedKeyCode;
    }
}