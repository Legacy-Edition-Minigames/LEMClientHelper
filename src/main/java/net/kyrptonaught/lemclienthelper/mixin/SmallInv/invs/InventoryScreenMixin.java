package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> implements SmallInvPlayerInv {
    @Shadow
    private float xMouse;
    @Shadow
    private float yMouse;
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LEMClientHelperMod.MOD_ID, "textures/gui/legacy_inventory.png");

    public InventoryScreenMixin(InventoryMenu screenHandler, RecipeBookComponent<?> recipeBookComponent, Inventory playerInventory, Component text) {
        super(screenHandler, recipeBookComponent, playerInventory, text);
    }

    @Inject(method = "renderLabels", at = @At("HEAD"), cancellable = true)
    public void smallInvTitle(GuiGraphics context, int mouseX, int mouseY, CallbackInfo ci) {
        if (getIsSmall()) {
            context.drawString(this.font, Component.translatable("container.inventory"), 6, 86, 0x404040, false);
            ci.cancel();
        }
    }

    @Inject(method = "renderBg", at = @At("HEAD"), cancellable = true)
    public void smallInv(GuiGraphics context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (getIsSmall()) {
            int k = this.leftPos;
            int l = this.topPos;
            this.imageHeight = 124;
            RecipeBookWidget.bookWidget.visible = false;
            context.blit(TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
            InventoryScreen.renderEntityInInventoryFollowsMouse(context, k + 26 + 52, l + 8 + 2, k + 75 + 52, l + 78 + 2, 30, 0.0625f, this.xMouse, this.yMouse, this.minecraft.player);
            ci.cancel();
        } else {
            this.imageHeight = 166;
            RecipeBookWidget.bookWidget.visible = true;
        }
    }

    @Override
    public boolean isSmallSupported() {
        return true;
    }
}