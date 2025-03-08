package net.kyrptonaught.lemclienthelper.mixin.hud.genericHud;

import net.kyrptonaught.lemclienthelper.hud.genericHud.PlayerBarRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class PlayerBarMixin {
    @Inject(method = "renderMainHud", at= @At(value = "HEAD"))
    void renderPlayerBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        PlayerBarRenderer.renderPlayerBar(context,tickCounter);
    }
}
