package net.kyrptonaught.lemclienthelper.TakeEverything;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TakeEverythingMod {
    public static KeyBinding takeEverythingKey;

    public static void onInitialize() {
        takeEverythingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(LEMClientHelperMod.MOD_ID + ".key.takeeverything", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_3, LEMClientHelperMod.MOD_ID + ".key.category.lemclienthelper"));
    }

    public static void registerControllerKeys() {
        LambdControlsCompat.register();
    }
}
