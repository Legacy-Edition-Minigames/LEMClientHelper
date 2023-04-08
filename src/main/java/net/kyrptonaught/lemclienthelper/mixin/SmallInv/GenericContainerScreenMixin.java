package net.kyrptonaught.lemclienthelper.mixin.SmallInv;

import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GenericContainerScreen.class)
public abstract class GenericContainerScreenMixin extends HandledScreen<GenericContainerScreenHandler> implements SmallInvPlayerInv {
    @Shadow
    @Final
    private int rows;

    public GenericContainerScreenMixin(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super((GenericContainerScreenHandler) handler, inventory, title);
    }

    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void drawSmalInv(MatrixStack matrices, float par2, int par3, int par4, CallbackInfo ci, int i, int j) {
        if (getIsSmall()) {
            drawTexture(matrices, i, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 13);
            drawTexture(matrices, i, j + (this.rows * 18 + 17) + 12, 0, 193, this.backgroundWidth, 28);
            ci.cancel();
        }
    }
}