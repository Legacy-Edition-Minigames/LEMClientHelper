package net.kyrptonaught.lemclienthelper.mixin.SyncedKeybinds;

import net.kyrptonaught.lemclienthelper.syncedKeybinds.GameOptionKeyExpander;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(Options.class)
public class GameOptionsMixin implements GameOptionKeyExpander {

    @Mutable
    @Final
    @Shadow
    public KeyMapping[] keyMappings;

    @Override
    public void addSyncedKeybinds(KeyMapping newKeybinding) {
        keyMappings = Arrays.copyOf(keyMappings, keyMappings.length + 1);

        keyMappings[keyMappings.length - 1] = newKeybinding;
    }

    @Override
    public void removeSyncedKeybinds(KeyMapping newKeybinding) {
        List<KeyMapping> bindings = new ArrayList<>(List.of(keyMappings));
        bindings.remove(newKeybinding);
        keyMappings = bindings.toArray(new KeyMapping[0]);
    }
}
