package net.kyrptonaught.lemclienthelper.mixin.AutoGG;

import net.kyrptonaught.lemclienthelper.AutoGG.AutoGGMod;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// AutoGG implemented by Terra <3

@Mixin(SoundManager.class)
public class SoundManagerMixin {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"))
    private void onPlay(SoundInstance sound, CallbackInfo ci) {
        Identifier id = sound.getId();

        if (id != null) {
            AutoGGMod.onSoundPlayed(id);
        }
    }
}
