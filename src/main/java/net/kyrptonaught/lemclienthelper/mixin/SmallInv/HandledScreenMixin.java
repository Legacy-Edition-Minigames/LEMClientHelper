package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.MovableSlot;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvInit;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
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

    protected HandledScreenMixin(Text title) {
        super(title);
    }
    //@Inject(method = "drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", at = @At(value = "HEAD"), cancellable = true)

    @Inject(method = "init", at = @At("RETURN"))
    public void setPlayerTitleY(CallbackInfo ci) {
        if (getIsSmall())
            if (((Object) this instanceof GenericContainerScreen)) this.playerInventoryTitleY += 3;
            else this.playerInventoryTitleY += 5;
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (SmallInvInit.isKeybindPressed(keyCode, false)) {
            setIsSmall(false);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (SmallInvInit.isKeybindPressed(button, true)) {
            setIsSmall(false);
            cir.setReturnValue(true);
        }
    }

    boolean isSmall = false;

    @Override
    public boolean getIsSmall() {
        return isSmall;
    }

    @Override
    public void setIsSmall(boolean small) {
        if (getIsSmall() && !small) playerInventoryTitleY -= 4;
        isSmall = small;
        int setY = -1;
        ScreenHandler handler = getScreenHandler();
        if (handler instanceof CreativeInventoryScreen.CreativeScreenHandler) return;
        for (int i = 0; i < handler.slots.size(); i++) {
            if (handler.slots.get(i) instanceof MovableSlot slot)
                if (handler instanceof PlayerScreenHandler) {
                    if (small)
                        SmallInvInit.tryMoveSlot(slot);
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

    @Override
    public void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if (isSmall) {
            HandledScreen<?> handledScreen = (HandledScreen<?>) (Object) this;
            if (!(handledScreen instanceof SmallInvScreen) && !(handledScreen instanceof InventoryScreen) &&
                    !(handledScreen instanceof CreativeInventoryScreen) && !(handledScreen instanceof GenericContainerScreen))
                if (u == 0 && v == 0) {
                    if (handledScreen instanceof ShulkerBoxScreen) height--;
                    super.drawTexture(matrices, x, y, 0, 0, width, height - 83); //shrink orig texture

                    super.drawTexture(matrices, x, y + 82, 0, height - 30, width, 29); //add extra separator
                    super.drawTexture(matrices, x, y + 82, 0, height - 86, width, 2); //draw hotbar
                    return;
                }
        }
        super.drawTexture(matrices, x, y, u, v, width, height);
    }
}