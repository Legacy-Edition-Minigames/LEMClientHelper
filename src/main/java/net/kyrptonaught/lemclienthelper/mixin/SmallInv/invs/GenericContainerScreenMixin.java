package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;


import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GenericContainerScreen.class)
public abstract class GenericContainerScreenMixin extends HandledScreen<GenericContainerScreenHandler> implements SmallInvPlayerInv {
    @Shadow
    @Final
    private int rows;

    public GenericContainerScreenMixin(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super((GenericContainerScreenHandler) handler, inventory, title);
    }

    @Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 1))
    public void drawSmallInv(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height) {
        if (getIsSmall()) {
            int j = (this.height - this.backgroundHeight) / 2;
            instance.drawTexture(texture, x, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 13);
            instance.drawTexture(texture, x, j + (this.rows * 18 + 17) + 12, 0, 193, this.backgroundWidth, 29);
        } else instance.drawTexture(texture, x, y, u, v, width, height);
    }

    @Override
    public boolean isSmallSupported() {
        return true;
    }
}