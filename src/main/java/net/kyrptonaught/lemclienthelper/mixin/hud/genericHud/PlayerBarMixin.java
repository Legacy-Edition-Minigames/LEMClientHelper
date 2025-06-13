package net.kyrptonaught.lemclienthelper.mixin.hud.genericHud;

import net.kyrptonaught.lemclienthelper.hud.genericHud.PlayerBarRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

@Mixin(Gui.class)
public class PlayerBarMixin {
    @Inject(method = "renderHotbarAndDecorations", at= @At(value = "HEAD"))
    void renderPlayerBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        PlayerBarRenderer.renderPlayerBar(guiGraphics,deltaTracker);
    }
}
