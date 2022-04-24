package net.kyrptonaught.lemclienthelper.mixin;

import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.TakeEverythingNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class ScreenHandlerMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void invsort$mouseClicked(double x, double y, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (LEMClientHelperMod.isKeybindPressed(button, true)) {
            TakeEverythingNetworking.sendSortPacket();
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void invsort$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (LEMClientHelperMod.isKeybindPressed(keycode, false)) {
            TakeEverythingNetworking.sendSortPacket();
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}