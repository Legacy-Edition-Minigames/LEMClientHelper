package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class SyncedKeybindsConfig implements AbstractConfigFile {

    public HashMap<ResourceLocation, KeybindConfigItem> keybinds = new HashMap<>();

    public static class KeybindConfigItem {
        public String keybinding;
        public String controllerBind;

        public String defaultKeybinding;
        public String defaultControllerbind;

        public KeybindConfigItem(String keybinding, String controllerBind) {
            this.keybinding = keybinding;
            this.controllerBind = controllerBind;
            updateDefaults(keybinding, controllerBind);
        }

        public void updateDefaults(String keybinding, String controllerBind) {
            this.defaultKeybinding = keybinding;
            this.defaultControllerbind = controllerBind;
        }
    }
}
