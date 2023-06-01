package net.kyrptonaught.lemclienthelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.kyrptonaught.lemclienthelper.ClientData.ClientDataMod;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloaderMod;
import net.kyrptonaught.lemclienthelper.ServerConfigs.ServerConfigsMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvMod;
import net.kyrptonaught.lemclienthelper.SpectateSqueaker.SpectateSqueakerMod;
import net.kyrptonaught.lemclienthelper.TakeEverything.TakeEverythingMod;
import net.kyrptonaught.lemclienthelper.hud.ArmorHud;
import net.kyrptonaught.lemclienthelper.syncedKeybinds.SyncedKeybindsMod;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class LEMClientHelperMod implements ClientModInitializer {
    public static final String MOD_ID = "lemclienthelper";
    public static ConfigManager.MultiConfigManager configManager = new ConfigManager.MultiConfigManager(MOD_ID);
    public static Identifier PRESENCE_PACKET = new Identifier("serverutils", "presence");

    @Override
    public void onInitializeClient() {
        TakeEverythingMod.onInitialize();
        ResourcePreloaderMod.onInitialize();
        SmallInvMod.onInitialize();
        ClientDataMod.onInitialize();
        SyncedKeybindsMod.onInitialize();
        SpectateSqueakerMod.onInitialize();
        ServerConfigsMod.onInitialize();

        // if (FabricLoader.getInstance().isModLoaded("lambdacontrols"))
        if (FabricLoader.getInstance().isModLoaded("midnightcontrols"))
            registerControllerKeys();
        //TODO: Implement this armor hud with config and or server integration
        //HudRenderCallback.EVENT.register(new ArmorHud());
        configManager.load();
    }

    public static void registerControllerKeys() {
        TakeEverythingMod.registerControllerKeys();
    }

    public static boolean isKeybindPressed(KeyBinding keyBinding, int pressedKeyCode, boolean isMouse) {
        InputUtil.Key keycode = KeyBindingHelper.getBoundKeyOf(keyBinding);

        if (isMouse) {
            if (keycode.getCategory() != InputUtil.Type.MOUSE) return false;
        } else {
            if (keycode.getCategory() != InputUtil.Type.KEYSYM) return false;
        }
        return keycode.getCode() == pressedKeyCode;
    }
}