package net.kyrptonaught.lemclienthelper.mixin.TakeEverything;

import net.kyrptonaught.lemclienthelper.TakeEverything.TakeEverythingMod;
import net.kyrptonaught.lemclienthelper.TakeEverything.TakeEverythingNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class ScreenHandlerMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void lephelper$mouseClicked(double x, double y, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (TakeEverythingMod.isKeybindPressed(button, InputUtil.Type.MOUSE)) {
            TakeEverythingNetworking.sendTakeEverythingPacket();
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void lebhelper$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (TakeEverythingMod.isKeybindPressed(keycode, InputUtil.Type.KEYSYM)) {
            TakeEverythingNetworking.sendTakeEverythingPacket();
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}