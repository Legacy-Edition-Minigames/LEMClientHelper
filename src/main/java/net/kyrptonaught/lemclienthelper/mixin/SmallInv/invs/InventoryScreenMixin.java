package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements SmallInvPlayerInv {
    @Shadow
    private float mouseX;
    @Shadow
    private float mouseY;
    private static final Identifier TEXTURE = new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/legacy_inventory.png");

    private static TexturedButtonWidget bookWidget;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @ModifyArg(method = "init", at = @At(target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", value = "INVOKE"))
    public Element fkRecipeBook(Element element) {
        if (element instanceof TexturedButtonWidget button)
            bookWidget = button;
        return element;
    }

    @Inject(method = "drawForeground", at = @At("HEAD"), cancellable = true)
    public void smallInvTitle(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci) {
        if (getIsSmall()) {
            this.textRenderer.draw(matrices, Text.translatable("container.inventory"), 6, 86, 0x404040);
            ci.cancel();
        }
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    public void smallInv(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (getIsSmall()) {
            this.backgroundHeight = 124;
            bookWidget.visible = false;
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, TEXTURE);
            drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            InventoryScreen.drawEntity(matrices, x + 51 + 51, y + 75 - 2, 30, (float) (x + 51 + 51) - this.mouseX, (float) (y + 75 - 50 - 2) - this.mouseY, this.client.player);
            ci.cancel();
        } else {
            this.backgroundHeight = 166;
            bookWidget.visible = true;
        }
    }

    @Override
    public boolean isSmallSupported() {
        return true;
    }
}