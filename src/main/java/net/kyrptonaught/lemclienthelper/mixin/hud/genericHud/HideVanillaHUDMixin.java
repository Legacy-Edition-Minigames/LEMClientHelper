package net.kyrptonaught.lemclienthelper.mixin.hud.genericHud;

import net.kyrptonaught.lemclienthelper.hud.genericHud.GenericHudMod;
import net.kyrptonaught.lemclienthelper.hud.genericHud.HideVanillaHUD;
import net.kyrptonaught.lemclienthelper.hud.genericHud.PlayerBarRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class HideVanillaHUDMixin {
    @Inject(method = "renderExperienceBar", at=@At("HEAD"), cancellable = true)
    void disableExperienceBar(GuiGraphics guiGraphics, int i, CallbackInfo ci) {
        if (!HideVanillaHUD.visibe.getValue(HideVanillaHUD.HUD_ELEMENT.EXPERIENCE) || !HideVanillaHUD.visibe.getValue(HideVanillaHUD.HUD_ELEMENT.ALL)) {ci.cancel();}
    }
}
