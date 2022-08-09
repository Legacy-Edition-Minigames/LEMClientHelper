package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import eu.midnightdust.midnightcontrols.client.controller.InputHandlers;
import net.minecraft.screen.slot.Slot;
import org.aperlambda.lambdacommon.utils.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(InputHandlers.class)
public class MidnightControlsMixin {

    @Inject(method = "lambda$handleInventorySlotPad$16", at = @At(value = "HEAD"), cancellable = true)
    private static void skipDisabledSlots(int guiLeft, int guiTop, double mouseX, double mouseY, Slot mouseSlot, int direction, Pair<Slot, Double> entry, CallbackInfoReturnable<Boolean> cir) {
        if (!entry.key.isEnabled()) cir.setReturnValue(false);
    }
}
