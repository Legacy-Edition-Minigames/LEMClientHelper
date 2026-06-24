package net.kyrptonaught.lemclienthelper.mixin.SpectateSquaker;

import net.kyrptonaught.lemclienthelper.SpectateSqueaker.SpectateSqueakerNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    public abstract @Nullable Entity getCameraEntity();

    @Shadow
    @Final
    public GameOptions options;

    @Inject(method = "handleInputEvents", at = @At(value = "TAIL"))
    public void trySqueak(CallbackInfo ci) {
        if (player != null && player.isSpectator() && (this.player.equals(getCameraEntity())) && options.attackKey.isPressed())
            SpectateSqueakerNetworking.sendSqueakPacket();
    }
}
