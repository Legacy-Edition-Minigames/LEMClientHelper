package net.kyrptonaught.lemclienthelper.mixin.SyncedKeybinds;

import net.kyrptonaught.lemclienthelper.syncedKeybinds.GameOptionKeyExpander;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(GameOptions.class)
public class GameOptionsMixin implements GameOptionKeyExpander {

    @Mutable
    @Final
    @Shadow
    public KeyBinding[] allKeys;

    @Override
    public void addSyncedKeybinds(KeyBinding newKeybinding) {
        allKeys = Arrays.copyOf(allKeys, allKeys.length + 1);

        allKeys[allKeys.length - 1] = newKeybinding;
    }

    @Override
    public void removeSyncedKeybinds(KeyBinding newKeybinding) {
        List<KeyBinding> bindings = new ArrayList<>(List.of(allKeys));
        bindings.remove(newKeybinding);
        allKeys = bindings.toArray(new KeyBinding[0]);
    }
}
