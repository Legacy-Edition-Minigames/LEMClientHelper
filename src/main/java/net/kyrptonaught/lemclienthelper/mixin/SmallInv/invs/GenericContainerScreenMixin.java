package net.kyrptonaught.lemclienthelper.mixin.SmallInv.invs;


import net.kyrptonaught.lemclienthelper.SmallInv.SmallInvPlayerInv;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ContainerScreen.class)
public abstract class GenericContainerScreenMixin extends AbstractContainerScreen<ChestMenu> implements SmallInvPlayerInv {
    @Shadow
    @Final
    private int containerRows;

    public GenericContainerScreenMixin(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super((ChestMenu) handler, inventory, title);
    }

    @Redirect(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 1))
    public void drawSmallInv(GuiGraphics instance, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {
        if (getIsSmall()) {
            int j = (this.height - this.imageHeight) / 2;
            instance.blit(texture, x, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 13);
            instance.blit(texture, x, j + (this.containerRows * 18 + 17) + 12, 0, 193, this.imageWidth, 29);
        } else instance.blit(texture, x, y, u, v, width, height);
    }

    @Override
    public boolean isSmallSupported() {
        return true;
    }
}