package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.MovableSlot;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public abstract class ScreenHandlerMixin {

    @Shadow
    @Final
    public NonNullList<Slot> slots;

    @Shadow
    @Final
    private NonNullList<ItemStack> lastSlots;

    @Shadow
    @Final
    private NonNullList<ItemStack> remoteSlots;

    @Inject(method = "addSlot", at = @At("HEAD"), cancellable = true)
    protected void addSlot(Slot slot, CallbackInfoReturnable<Slot> cir) {
        if (isPlayerScreen() || slot.container instanceof Inventory) {
            slot = new MovableSlot(slot);
            slot.index = this.slots.size();
            this.slots.add(slot);
            this.lastSlots.add(ItemStack.EMPTY);
            this.remoteSlots.add(ItemStack.EMPTY);
            cir.setReturnValue(slot);
        }
    }

    @Unique
    private boolean isPlayerScreen() {
        //this works but IDEA big mad
        return (Object) this instanceof InventoryMenu;
    }
}