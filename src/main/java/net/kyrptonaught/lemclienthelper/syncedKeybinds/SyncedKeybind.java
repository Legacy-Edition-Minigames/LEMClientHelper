package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.kyrptonaught.kyrptconfig.keybinding.CustomKeyBinding;
import net.kyrptonaught.kyrptconfig.keybinding.DisplayOnlyKeyBind;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.option.KeyBinding;

public class SyncedKeybind {
    public String ID;
    CustomKeyBinding keyBinding;
    private KeyBinding vanillaBind;

    public SyncedKeybind(String id, String keybinding, String controllerBind, String defaultKey) {
        this.ID = id;
        keyBinding = CustomKeyBinding.configDefault(SyncedKeybinds.MOD_ID, defaultKey);
        keyBinding.setRaw(keybinding);

    }

    public KeyBinding getVanillaBind() {
        if (vanillaBind == null)
            vanillaBind = new DisplayOnlyKeyBind(
                    "lch.key.sync." + ID.replace(":", "."),
                    "key.category." + LEMClientHelperMod.MOD_ID,
                    keyBinding,
                    setKey -> {
                        SyncedKeybinds.getConfig().keybinds.get(ID).keybinding = keyBinding.rawKey;
                        LEMClientHelperMod.configManager.save(SyncedKeybinds.MOD_ID);
                    }
            );
        return vanillaBind;
    }
}
