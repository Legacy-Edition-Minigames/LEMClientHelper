package net.kyrptonaught.lemclienthelper.mixin.SpectateSquaker;

import net.kyrptonaught.lemclienthelper.SpectateSqueaker.SpectateSqueakerNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"))
    public void trySqueak(CallbackInfoReturnable<Boolean> cir) {
        if (player != null && player.isSpectator())
            SpectateSqueakerNetworking.sendTakeEverythingPacket();
    }
}
