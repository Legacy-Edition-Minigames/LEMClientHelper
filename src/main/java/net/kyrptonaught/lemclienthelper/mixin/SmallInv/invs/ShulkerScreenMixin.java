package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerScreenMixin implements SmallInvPlayerInv {

    @Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/ShulkerBoxScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    public void drawSmalInv(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if (getIsSmall()) {
            DrawableHelper.drawTexture(matrices, x, y, 0, 0, width, height - 83); //shrink orig texture

            DrawableHelper.drawTexture(matrices, x, y + (height - 83) - 1, 0, height - 30, width, 29);//draw hotbar
            DrawableHelper.drawTexture(matrices, x, y + (height - 83) - 1, 0, height - 86, width, 2); //add extra separator
        }
    }
}
