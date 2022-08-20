package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

import java.util.HashMap;

public class SyncedKeybindsConfig implements AbstractConfigFile {

    public HashMap<String, KeybindConfigItem> keybinds = new HashMap<>();

    public static class KeybindConfigItem {
        public String keybinding;
        public String controllerBind;

        public KeybindConfigItem(String keybinding, String controllerBind) {
            this.keybinding = keybinding;
            this.controllerBind = controllerBind;
        }
    }
}
