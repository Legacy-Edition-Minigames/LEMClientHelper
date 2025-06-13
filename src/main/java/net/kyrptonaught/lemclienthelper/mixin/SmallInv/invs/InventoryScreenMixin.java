package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> implements SmallInvPlayerInv {
    @Shadow
    private float xMouse;
    @Shadow
    private float yMouse;
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LEMClientHelperMod.MOD_ID, "textures/gui/legacy_inventory.png");

    private static ImageButton bookWidget;

    public InventoryScreenMixin(InventoryMenu screenHandler, Inventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
    }

    @ModifyArg(method = "init", at = @At(target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", value = "INVOKE"))
    public GuiEventListener fkRecipeBook(GuiEventListener element) {
        if (element instanceof ImageButton button)
            bookWidget = button;
        return element;
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
            bookWidget.visible = false;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            context.blit(TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
            InventoryScreen.renderEntityInInventoryFollowsMouse(context, k + 26 + 52, l + 8 + 2, k + 75 + 52, l + 78 + 2, 30, 0.0625f, this.xMouse, this.yMouse, this.minecraft.player);
            ci.cancel();
        } else {
            this.imageHeight = 166;
            bookWidget.visible = true;
        }
    }

    @Override
    public boolean isSmallSupported() {
        return true;
    }
}