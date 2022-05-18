package net.kyrptonaught.lemclienthelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.kyrptonaught.lemclienthelper.ResourcePreloader.ResourcePreloader;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvInit;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class LEMClientHelperMod implements ClientModInitializer {
    public static final String MOD_ID = "lemclienthelper";
    public static KeyBinding takeEverythingKey;
    public static ConfigManager configManager = new ConfigManager.MultiConfigManager(MOD_ID);

    @Override
    public void onInitializeClient() {
        takeEverythingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(MOD_ID + ".key.takeeverything", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_3, MOD_ID + ".key.category.lemclienthelper"));
        ResourcePreloader.init();
        configManager.load();
        // if (FabricLoader.getInstance().isModLoaded("lambdacontrols"))
        //LambdControlsCompat.register();
        SmallInvInit.init();
    }

    public static boolean isKeybindPressed(int pressedKeyCode, boolean isMouse) {
        InputUtil.Key keycode = KeyBindingHelper.getBoundKeyOf(takeEverythingKey);

        if (isMouse) {
            if (keycode.getCategory() != InputUtil.Type.MOUSE) return false;
        } else {
            if (keycode.getCategory() != InputUtil.Type.KEYSYM) return false;
        }
        return keycode.getCode() == pressedKeyCode;
    }
}