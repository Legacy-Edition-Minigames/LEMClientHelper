package net.kyrptonaught.lemclienthelper.syncedKeybinds;

import net.minecraft.client.KeyMapping;

public interface GameOptionKeyExpander {

    void addSyncedKeybinds(KeyMapping newKeybinding);

    void removeSyncedKeybinds(KeyMapping newKeybinding);
}
