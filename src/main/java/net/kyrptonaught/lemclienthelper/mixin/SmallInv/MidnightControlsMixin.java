package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import eu.midnightdust.midnightcontrols.client.controller.InputHandlers;
import eu.midnightdust.midnightcontrols.client.enums.ButtonState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.Slot;
import org.aperlambda.lambdacommon.utils.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(InputHandlers.class)
public class MidnightControlsMixin {

    @Inject(method = "lambda$handleInventorySlotPad$9", at = @At(value = "HEAD"), cancellable = true)
    private static void skipDisabledSlots(int direction, Minecraft client, ButtonBinding binding, float value, ButtonState action, CallbackInfoReturnable<Boolean> cir) {
        if (!binding.isAvailable()) cir.setReturnValue(false);
    }
}
