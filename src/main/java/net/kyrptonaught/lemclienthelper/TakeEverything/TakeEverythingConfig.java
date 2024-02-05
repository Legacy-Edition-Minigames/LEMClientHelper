package net.kyrptonaught.lemclienthelper.TakeEverything;

import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;
import net.kyrptonaught.kyrptconfig.keybinding.CustomKeyBinding;

public class TakeEverythingConfig implements AbstractConfigFile {

    public boolean enabled = true;

    public CustomKeyBinding keybinding = CustomKeyBinding.configDefault(TakeEverythingMod.MOD_ID, "key.keyboard.space");
}
