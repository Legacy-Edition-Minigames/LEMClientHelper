package net.kyrptonaught.lemclienthelper.mixin.TextShadow;

import net.kyrptonaught.lemclienthelper.TextShadow.StyleWShadow;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    public int drawWShadowType(TextRenderer instance, MatrixStack matrices, Text text, float x, float y, int color) {
        if (text.getStyle() instanceof StyleWShadow style) {
            StyleWShadow.ShadowType shadowType = style.hasShadow();
            if (shadowType == StyleWShadow.ShadowType.FORCE)
                return instance.drawWithShadow(matrices, text, x, y, color);
            else if (shadowType == StyleWShadow.ShadowType.HIDE)
                return instance.draw(matrices, text, x, y, color);

        }

        return instance.drawWithShadow(matrices, text, x, y, color);
    }
}
