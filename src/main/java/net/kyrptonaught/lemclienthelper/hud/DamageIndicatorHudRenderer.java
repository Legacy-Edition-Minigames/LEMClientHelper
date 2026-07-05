package net.kyrptonaught.lemclienthelper.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.util.List;

public class DamageIndicatorHudRenderer {
    private static final Identifier[] INDICATORS = new Identifier[]{
            new Identifier("lemclienthelper", "textures/hud/damage_indicator_0.png"),
            new Identifier("lemclienthelper", "textures/hud/damage_indicator_1.png"),
            new Identifier("lemclienthelper", "textures/hud/damage_indicator_2.png"),
            new Identifier("lemclienthelper", "textures/hud/damage_indicator_3.png"),
            new Identifier("lemclienthelper", "textures/hud/damage_indicator_4.png"),
            new Identifier("lemclienthelper", "textures/hud/damage_indicator_5.png"),
            new Identifier("lemclienthelper", "textures/hud/damage_indicator_6.png"),
            new Identifier("lemclienthelper", "textures/hud/damage_indicator_7.png")
    };

    public static void onHudRender(DrawContext context, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && HudMod.shouldDisplayDmgIndicator()) {
            int height = client.getWindow().getScaledHeight();
            int width = client.getWindow().getScaledWidth();

            context.getMatrices().push();
            context.getMatrices().translate(width / 2f, height / 2f, 0);
            context.getMatrices().scale(1f, 1f, 1f);
            context.getMatrices().translate(-8.5, -8, 0);
            context.setShaderColor(1f, 1f, 1f, 1f);

            if (HudMod.shouldDisplayDmgIndicator()) {
                int[] time = HudMod.getDMGTimeAngle();
                draw(context, INDICATORS[0], 0, 0, Math.max(1 - (time[0] / ((float) HudMod.getConfig().damageIndicatorFadeOut)), 0));
                draw(context, INDICATORS[1], 0, 0, Math.max(1 - (time[1] / ((float) HudMod.getConfig().damageIndicatorFadeOut)), 0));
                draw(context, INDICATORS[2], 0, 0, Math.max(1 - (time[2] / ((float) HudMod.getConfig().damageIndicatorFadeOut)), 0));
                draw(context, INDICATORS[3], 0, 0, Math.max(1 - (time[3] / ((float) HudMod.getConfig().damageIndicatorFadeOut)), 0));
                draw(context, INDICATORS[4], 0, 0, Math.max(1 - (time[4] / ((float) HudMod.getConfig().damageIndicatorFadeOut)), 0));
                draw(context, INDICATORS[5], 0, 0, Math.max(1 - (time[5] / ((float) HudMod.getConfig().damageIndicatorFadeOut)), 0));
                draw(context, INDICATORS[6], 0, 0, Math.max(1 - (time[6] / ((float) HudMod.getConfig().damageIndicatorFadeOut)), 0));
                draw(context, INDICATORS[7], 0, 0, Math.max(1 - (time[7] / ((float) HudMod.getConfig().damageIndicatorFadeOut)), 0));
            }
            context.setShaderColor(1f, 1f, 1f, 1f);
            context.getMatrices().pop();
        }
    }

    private static void draw(DrawContext context, Identifier texture, float x, float y, float alpha) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferBuilder.vertex(matrix4f, x, y, 0f).color(1f, 1f, 1f,  alpha).texture(0f, 0f).next();
        bufferBuilder.vertex(matrix4f, x, y + 16, 0f).color(1, 1f, 1f,  alpha).texture(0f, 1f).next();
        bufferBuilder.vertex(matrix4f, x + 16, y + 16, 0f).color(1f, 1f, 1f,  alpha).texture(1f, 1f).next();
        bufferBuilder.vertex(matrix4f, x + 16, y, 0f).color(1f, 1f, 1f,  alpha).texture(1f, 0f).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }
}
