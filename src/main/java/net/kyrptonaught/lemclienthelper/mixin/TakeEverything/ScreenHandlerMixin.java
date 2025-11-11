package net.kyrptonaught.lemclienthelper.mixin.TakeEverything;

import com.mojang.blaze3d.platform.InputConstants;
import net.kyrptonaught.lemclienthelper.TakeEverything.TakeEverythingMod;
import net.kyrptonaught.lemclienthelper.TakeEverything.TakeEverythingNetworking;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class ScreenHandlerMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void lephelper$mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (TakeEverythingMod.isKeybindPressed(mouseButtonEvent.button(), InputConstants.Type.MOUSE)) {
            TakeEverythingNetworking.sendTakeEverythingPacket();
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void lebhelper$keyPressed(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (TakeEverythingMod.isKeybindPressed(keyEvent.key(), InputConstants.Type.KEYSYM)) {
            TakeEverythingNetworking.sendTakeEverythingPacket();
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}