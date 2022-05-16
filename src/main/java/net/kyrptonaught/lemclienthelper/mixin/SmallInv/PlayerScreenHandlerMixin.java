package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.MovableSlot;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvInit;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements SmallInvPlayerInv {
    protected PlayerScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    //todo bad
    @Override
    protected Slot addSlot(Slot slot) {
        return super.addSlot(new MovableSlot(slot));
    }

    private boolean smallInv;

    @Override
    public boolean isSmall() {
        return smallInv;
    }

    @Override
    public void setIsSmall(boolean small) {
        this.smallInv = small;
        for (Slot slot : this.slots) {
            if (small)
                SmallInvInit.checkSlot((MovableSlot) slot);
            else
                ((MovableSlot) slot).resetPos();
        }
    }
}