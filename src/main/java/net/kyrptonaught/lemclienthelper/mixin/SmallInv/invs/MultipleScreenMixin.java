package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {
        BeaconScreen.class,
        BrewingStandScreen.class,
        CartographyTableScreen.class,
        CraftingScreen.class,
        EnchantmentScreen.class,
        ForgingScreen.class,
        AbstractFurnaceScreen.class,
        Generic3x3ContainerScreen.class,
        GrindstoneScreen.class,
        HopperScreen.class,
        HorseScreen.class,
        LoomScreen.class,
        ShulkerBoxScreen.class,
        StonecutterScreen.class
})
public abstract class MultipleScreenMixin implements SmallInvPlayerInv {

    @Redirect(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0))
    public void drawSmallInv(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height) {
        if (getIsSmall()) {
            instance.drawTexture(texture, x, y, 0, 0, width, height - 83); //shrink orig texture

            instance.drawTexture(texture, x, y + (height - 83) - 1, 0, height - 30, width, 29);//draw hotbar
            instance.drawTexture(texture, x, y + (height - 83) - 1, 0, height - 86, width, 2); //add extra separator
        } else instance.drawTexture(texture, x, y, u, v, width, height);
    }

    @Override
    public boolean isSmallSupported() {
        return true;
    }
}
