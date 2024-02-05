package net.kyrptonaught.lemclienthelper.TakeEverything;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.NonConflictingKeyBinding;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class TakeEverythingMod {
    public static String MOD_ID = "take_everything";

    public static KeyBinding takeEverythingKey;

    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new TakeEverythingConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);

        takeEverythingKey = KeyBindingHelper.registerKeyBinding(new NonConflictingKeyBinding(
                LEMClientHelperMod.MOD_ID + ".key.takeeverything",
                "key.category." + LEMClientHelperMod.MOD_ID,
                getConfig().keybinding,
                setKey -> LEMClientHelperMod.configManager.save(MOD_ID)
        ));
    }

    public static TakeEverythingConfig getConfig() {
        return (TakeEverythingConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }

    public static void registerControllerKeys() {
        LambdControlsCompat.register();
    }

    public static boolean isKeybindPressed(int pressedKeyCode, InputUtil.Type type) {
        return getConfig().keybinding.matches(pressedKeyCode, type);
    }
}
