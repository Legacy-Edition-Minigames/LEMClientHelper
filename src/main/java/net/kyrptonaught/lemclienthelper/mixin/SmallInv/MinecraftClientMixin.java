package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "setScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", ordinal = 2))
    public void attemptOpenSmallInv(Screen screen, CallbackInfo ci) {
        if (screen instanceof HandledScreen<?> handledScreen) {
            ((SmallInvPlayerInv) handledScreen).setIsSmall(SmallInvMod.isSmallInv(player));
        }
    }
}