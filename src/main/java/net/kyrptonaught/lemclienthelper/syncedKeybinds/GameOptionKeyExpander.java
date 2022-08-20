package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.minecraft.client.option.KeyBinding;

public interface GameOptionKeyExpander {

    void addSyncedKeybinds(KeyBinding newKeybinding);

    void removeSyncedKeybinds(KeyBinding newKeybinding);
}
