package net.kyrptonaught.lemclienthelper.mixin.hud.genericHud;

import net.kyrptonaught.lemclienthelper.hud.genericHud.GenericHudMod;
import net.kyrptonaught.lemclienthelper.hud.genericHud.PlayerBarRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class PlayerBarMixin {
    @Inject(method = "renderHotbarAndDecorations", at= @At(value = "HEAD"))
    void renderPlayerBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        PlayerBarRenderer.renderPlayerBar(guiGraphics,deltaTracker);
    }

    @Inject(method = "renderExperienceBar", at=@At("HEAD"), cancellable = true)
    void disableExperienceBar(GuiGraphics guiGraphics, int i, CallbackInfo ci) {
        if (GenericHudMod.SHOULD_RENDER_PLAYERBAR) {ci.cancel();}
    }
}
