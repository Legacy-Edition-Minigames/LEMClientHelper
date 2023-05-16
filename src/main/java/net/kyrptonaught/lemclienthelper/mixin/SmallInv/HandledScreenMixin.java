package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.MovableSlot;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen implements SmallInvPlayerInv {

    @Shadow
    protected int playerInventoryTitleY;

    @Shadow
    public abstract ScreenHandler getScreenHandler();

    @Shadow
    protected int backgroundHeight;

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void setPlayerTitleY(CallbackInfo ci) {
        if (getIsSmall())
            if (((Object) this instanceof GenericContainerScreen)) this.playerInventoryTitleY += 3;
            else this.playerInventoryTitleY += 5;
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (SmallInvMod.isKeybindPressed(keyCode, false)) {
            setIsSmall(false);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (SmallInvMod.isKeybindPressed(button, true)) {
            setIsSmall(false);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isClickOutsideBounds", at = @At("HEAD"), cancellable = true)
    public void isClickOutsideSmallBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> cir) {
        if (getIsSmall()) {
            HandledScreen<?> handledScreen = (HandledScreen<?>) (Object) this;
            if (!(handledScreen instanceof InventoryScreen) &&
                    !(handledScreen instanceof CreativeInventoryScreen))
                if (mouseY >= (top + (this.backgroundHeight - 85 + 30))) cir.setReturnValue(true);
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

        if (getIsSmall() && !small) playerInventoryTitleY -= 4;
        isSmall = small;
        int setY = -1;
        ScreenHandler handler = getScreenHandler();
        if (handler instanceof CreativeInventoryScreen.CreativeScreenHandler) return;
        for (int i = 0; i < handler.slots.size(); i++) {
            if (handler.slots.get(i) instanceof MovableSlot slot)
                if (handler instanceof PlayerScreenHandler) {
                    if (small)
                        SmallInvMod.tryMoveSlot(slot);
                    else slot.resetPos();
                } else {
                    if (small) {
                        if (setY == -1) {
                            setY = slot.y + 4;
                            if (handler instanceof GenericContainerScreenHandler) setY--;
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