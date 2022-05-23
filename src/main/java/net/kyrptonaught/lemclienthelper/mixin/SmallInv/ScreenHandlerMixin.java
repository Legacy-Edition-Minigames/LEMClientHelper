package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.MovableSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow
    @Final
    public DefaultedList<Slot> slots;

    @Shadow
    @Final
    private DefaultedList<ItemStack> trackedStacks;

    @Shadow
    @Final
    private DefaultedList<ItemStack> previousTrackedStacks;

    @Inject(method = "addSlot", at = @At("HEAD"), cancellable = true)
    protected void addSlot(Slot slot, CallbackInfoReturnable<Slot> cir) {
        if (isPlayerScreen() || slot.inventory instanceof PlayerInventory) {
            slot = new MovableSlot(slot);
            slot.id = this.slots.size();
            this.slots.add(slot);
            this.trackedStacks.add(ItemStack.EMPTY);
            this.previousTrackedStacks.add(ItemStack.EMPTY);
            cir.setReturnValue(slot);
        }
    }

    @Unique
    private boolean isPlayerScreen() {
        //this works but IDEA big mad
        return (Object) this instanceof PlayerScreenHandler;
    }
}