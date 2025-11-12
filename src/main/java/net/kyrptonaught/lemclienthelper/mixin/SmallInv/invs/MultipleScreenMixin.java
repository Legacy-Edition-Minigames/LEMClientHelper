package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.renderer.RenderPipelines;
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

    @Redirect(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 0))
    public void drawSmallInv(GuiGraphics instance, RenderPipeline pipeline, ResourceLocation texture, int x, int y, float f, float g, int u, int v, int width, int height) {
        if (getIsSmall()) {

            if ((AbstractContainerScreen<?>) (Object) this instanceof ShulkerBoxScreen) height--;
            instance.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, f, g, 0, 0, width, height - 83); //shrink orig texture

            instance.blit(RenderPipelines.GUI_TEXTURED, texture, x, y + (height - 83) - 1, f, g, 0, height - 30, width, 30);//draw hotbar
            instance.blit(RenderPipelines.GUI_TEXTURED, texture, x, y + (height - 83) - 1, f, g, 0, height - 86, width, 2); //add extra separator
        } else instance.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, f, g, u, v, width, height);
    }

    @Override
    public boolean isSmallSupported() {
        return true;
    }
}
