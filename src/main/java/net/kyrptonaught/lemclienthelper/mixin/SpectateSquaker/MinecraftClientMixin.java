package net.kyrptonaught.lemclienthelper.mixin.SpectateSquaker;

import net.kyrptonaught.lemclienthelper.SpectateSqueaker.SpectateSqueakerNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    public abstract @Nullable Entity getCameraEntity();

    @Shadow
    @Final
    public Options options;

    @Inject(method = "handleKeybinds", at = @At(value = "TAIL"))
    public void trySqueak(CallbackInfo ci) {
        if (player != null && player.isSpectator() && (this.player.equals(getCameraEntity())) && options.keyAttack.isDown())
            SpectateSqueakerNetworking.sendSqueakPacket();
    }
}
