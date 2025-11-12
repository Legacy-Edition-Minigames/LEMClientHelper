package net.kyrptonaught.lemclienthelper.mixin.hud.genericHud;


import net.kyrptonaught.lemclienthelper.hud.genericHud.HideVanillaHUD;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class HideVanillaHUDMixin {
    @Inject(method = "renderHotbarAndDecorations", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"), cancellable = true)
    void disableContextualBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.EXPERIENCE, true) || !HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.ALL,true)) {ci.cancel();}
    }
    @Inject(method = "renderHotbarAndDecorations", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;renderBackground(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"), cancellable = true)
    void disableContextualBarBg(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.EXPERIENCE, true) || !HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.ALL,true)) {ci.cancel();}
    }

    @Inject(method = "renderHotbarAndDecorations", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;renderExperienceLevel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;I)V"), cancellable = true)
    void disableExperienceLevel(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.EXPERIENCE, true) || !HideVanillaHUD.visible.getOrDefault(HideVanillaHUD.HUD_ELEMENT.ALL,true)) {ci.cancel();}
    }
}
