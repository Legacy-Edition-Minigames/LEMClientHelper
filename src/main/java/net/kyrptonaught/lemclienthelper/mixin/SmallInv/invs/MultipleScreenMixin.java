package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {
        BeaconScreen.class,
        BrewingStandScreen.class,
        CartographyTableScreen.class,
        CraftingScreen.class,
        EnchantmentScreen.class,
        ItemCombinerScreen.class,
        AbstractFurnaceScreen.class,
        DispenserScreen.class,
        GrindstoneScreen.class,
        HopperScreen.class,
        HorseInventoryScreen.class,
        LoomScreen.class,
        ShulkerBoxScreen.class,
        StonecutterScreen.class
})
public abstract class MultipleScreenMixin implements SmallInvPlayerInv {

    @Redirect(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 0))
    public void drawSmallInv(GuiGraphics instance, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {
        if (getIsSmall()) {

            if ((AbstractContainerScreen<?>) (Object) this instanceof ShulkerBoxScreen) height--;
            instance.blit(texture, x, y, 0, 0, width, height - 83); //shrink orig texture

            instance.blit(texture, x, y + (height - 83) - 1, 0, height - 30, width, 30);//draw hotbar
            instance.blit(texture, x, y + (height - 83) - 1, 0, height - 86, width, 2); //add extra separator
        } else instance.blit(texture, x, y, u, v, width, height);
    }

    @Override
    public boolean isSmallSupported() {
        return true;
    }
}
