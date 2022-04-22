package net.kyrptonaught.lebclienthelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class LEBClientHelperMod implements ClientModInitializer {
    public static final String MOD_ID = "lebclienthelper";
    public static KeyBinding takeEverythingKey;

    @Override
    public void onInitializeClient() {
        takeEverythingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(MOD_ID + ".key.takeeverything", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_3, MOD_ID + ".key.category.lebclienthelper"));
        // if (FabricLoader.getInstance().isModLoaded("lambdacontrols"))
        //LambdControlsCompat.register();
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
