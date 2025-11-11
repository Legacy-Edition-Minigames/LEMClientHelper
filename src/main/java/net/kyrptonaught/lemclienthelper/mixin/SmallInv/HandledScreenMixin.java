package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.MovableSlot;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin extends Screen implements SmallInvPlayerInv {

    @Shadow
    protected int inventoryLabelY;

    @Shadow
    public abstract AbstractContainerMenu getMenu();

    @Shadow
    protected int imageHeight;

    protected HandledScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void setPlayerTitleY(CallbackInfo ci) {
        if (getIsSmall())
            if (((Object) this instanceof ContainerScreen)) this.inventoryLabelY += 3;
            else this.inventoryLabelY += 5;
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if (SmallInvMod.isKeybindPressed(keyEvent.key(), false)) {
            setIsSmall(false);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (SmallInvMod.isKeybindPressed(mouseButtonEvent.button(), true)) {
            setIsSmall(false);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hasClickedOutside", at = @At("HEAD"), cancellable = true)
    public void isClickOutsideSmallBounds(double mouseX, double mouseY, int left, int top, CallbackInfoReturnable<Boolean> cir) {
        if (getIsSmall()) {
            AbstractContainerScreen<?> handledScreen = (AbstractContainerScreen<?>) (Object) this;
            if (!(handledScreen instanceof InventoryScreen) &&
                    !(handledScreen instanceof CreativeModeInventoryScreen))
                if (mouseY >= (top + (this.imageHeight - 85 + 30))) cir.setReturnValue(true);
        }
    }

    boolean isSmall = false;

    @Override
    public boolean getIsSmall() {
        return isSmall;
    }

    @Override
    public void setIsSmall(boolean small) {
        if (!this.isSmallSupported()) return;

        if (getIsSmall() && !small) inventoryLabelY -= 4;
        isSmall = small;
        int setY = -1;
        AbstractContainerMenu handler = getMenu();
        if (handler instanceof CreativeModeInventoryScreen.ItemPickerMenu) return;
        for (int i = 0; i < handler.slots.size(); i++) {
            if (handler.slots.get(i) instanceof MovableSlot slot)
                if (handler instanceof InventoryMenu) {
                    if (small)
                        SmallInvMod.tryMoveSlot(slot);
                    else slot.resetPos();
                } else {
                    if (small) {
                        if (setY == -1) {
                            setY = slot.y + 4;
                            if (handler instanceof ChestMenu) setY--;
                        }
                        if (i >= handler.slots.size() - 9) {
                            slot.setPos(slot.x, setY);
                            slot.isEnabled = true;
                        } else slot.isEnabled = false;
                    } else slot.resetPos();
                }
        }
    }
}