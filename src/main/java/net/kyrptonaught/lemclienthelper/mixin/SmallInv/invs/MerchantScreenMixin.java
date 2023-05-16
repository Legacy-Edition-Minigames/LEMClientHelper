package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin implements SmallInvPlayerInv {

    @Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/MerchantScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIFFIIII)V", ordinal = 0))
    public void drawSmallInvdrawTexture(MatrixStack matrices, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        if (getIsSmall()) {
            DrawableHelper.drawTexture(matrices, x, y, 0, 0, width, height - 83); //shrink orig texture

            DrawableHelper.drawTexture(matrices, x, y + (height - 83) - 1, 0, height - 30, width, 29);//draw hotbar
            DrawableHelper.drawTexture(matrices, x, y + (height - 83) - 1, 0, height - 86, width, 2); //add extra separator
        } else DrawableHelper.drawTexture(matrices, x, y, z, u, v, width, height, textureWidth, textureHeight);
    }

    @Override
    public boolean isSmallSupported() {
        return false;
    }
}
