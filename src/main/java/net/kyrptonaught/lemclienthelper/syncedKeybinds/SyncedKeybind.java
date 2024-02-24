package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.kyrptonaught.kyrptconfig.config.NonConflicting.NonConflictingKeyBinding;
import net.kyrptonaught.kyrptconfig.keybinding.CustomKeyBinding;
import net.kyrptonaught.kyrptconfig.keybinding.DisplayOnlyKeyBind;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.option.KeyBinding;

public class SyncedKeybind {
    public String ID;
    private final CustomKeyBinding keyBinding;
    private KeyBinding vanillaBind;

    public SyncedKeybind(String id, SyncedKeybindsConfig.KeybindConfigItem keybindConfigItem) {
        this.ID = id;
        keyBinding = CustomKeyBinding.configDefault(SyncedKeybindsMod.MOD_ID, keybindConfigItem.defaultKeybinding);
        keyBinding.setRaw(keybindConfigItem.keybinding);
    }

    public boolean wasPressed() {
        return keyBinding.wasPressed();
    }

    public boolean isHeld() {
        return keyBinding.isKeybindPressed();
    }

    public KeyBinding getVanillaBind() {
        if (vanillaBind == null)
            vanillaBind = new NonConflictingKeyBinding(
                    "lch.key.sync." + ID.replace(":", "."),
                    "key.category." + LEMClientHelperMod.MOD_ID,
                    keyBinding,
                    setKey -> {
                        SyncedKeybindsMod.getConfig().keybinds.get(ID).keybinding = keyBinding.rawKey;
                        LEMClientHelperMod.configManager.save(SyncedKeybindsMod.MOD_ID);
                    }
            );
        return vanillaBind;
    }

    public void updateBoundKey(String key) {
        keyBinding.setRaw(key);
        ((DisplayOnlyKeyBind) getVanillaBind()).updateSetKey();
    }
}
