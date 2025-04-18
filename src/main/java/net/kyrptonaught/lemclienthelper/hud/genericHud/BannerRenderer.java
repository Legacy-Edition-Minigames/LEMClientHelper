package net.kyrptonaught.lemclienthelper.hud.genericHud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.LogManager;

import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.GAME_MODES.*;
import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.MINIGAME_PHASES.*;
import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.MINIGAME_TYPES.*;

public class BannerRenderer {
    private static final Identifier SHIELD = Identifier.of("lemclienthelper","textures/gui/shield.png");
    private static final Identifier BANNER = Identifier.of("lemclienthelper","textures/gui/banner.png");
    private static final Identifier PARTICLE = Identifier.of("minecraft","textures/particle/effect_4.png");
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final TextRenderer textRenderer = client.textRenderer;

    private static float elapsed = 0.0f;
    private static float elapsedMax = 3.0f;

    private static float shieldScale = 0.0f;
    private static float bannerScale = 0.0f;
    private static float bannerWidth = 0.0f;
    private static float textTrans = 0.0f;

    private static Identifier shield_icon = Identifier.of("lemclienthelper","textures/gui/battle/shield_icon.png");

    public static void onHudRender(DrawContext context, RenderTickCounter v) {
        if (GenericHudMod.BANNER_RECEIVED) {
            float lastDur = (v.getLastFrameDuration() / 20f);
            render(context, lastDur);
        }
    }

    public static void render(DrawContext context, float lastDur) {
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        elapsed += lastDur;

        int x = (width/2) - (int)((24 * shieldScale)/2);
        int y = ((13 * height)/50) - (int)((34 * shieldScale)/2);

        context.getMatrices().push();
        RenderSystem.enableBlend();

        if (elapsed >= 0.1f) { renderParticles(context,elapsed,(width/2),height); }
        renderShield(context,x,y);
        if (elapsed >= (16f/30f)) { renderBanner(context,elapsed,width,height); }

        context.getMatrices().pop();

        if (elapsed >= elapsedMax) {
            reset();
        }
    }

    private static void reset() {
        GenericHudMod.BANNER_RECEIVED = false;
        elapsed = 0.0f;
        shieldScale = 0.0f;
        bannerScale = 0.0f;
        bannerWidth = 0.0f;
        textTrans = 0.0f;
    }

    private static void renderBanner(DrawContext context, float elapsed, int width, int height) {
        bannerScale = MathHelper.lerp((elapsed/(24f/30f)), bannerScale,2.4f);
        bannerWidth = MathHelper.lerp((elapsed/(24f/30f)), bannerWidth, (textRenderer.getWidth(GenericHudMod.BANNER_TEXT)-((4*(int)bannerScale))));
        textTrans = MathHelper.lerp((elapsed/(40f/30f)),textTrans,1.0f);

        int x = (width/2) - (int)((bannerWidth)/2);
        int y = ((13 * height)/50) + (int)((7.5*((2*bannerScale)/2.4f)));

        context.drawTexture(BANNER, (x-(7*(int)bannerScale)), y,(7*(int)(bannerScale)),8*(int)(bannerScale),0,0,7,8,31,8);
        context.drawTexture(BANNER, (x),   y, (int) bannerWidth,8*(int)(bannerScale), 7*(int)(bannerScale),0, 1,8,31,8);
        context.drawTexture(BANNER, ((width/2)+(int)((bannerWidth)/2)),   y,7*(int)(bannerScale),8*(int)(bannerScale),24,0,7,8,31,8);

        context.getMatrices().translate(0f,0.25f,0f);
        RenderSystem.setShaderColor(1,1,1,textTrans);
        context.drawText(textRenderer,GenericHudMod.BANNER_TEXT,x-((2*(int)bannerScale)),y+2,0x444444,false);
        RenderSystem.setShaderColor(1,1,1,1);
        context.getMatrices().translate(0f,-0.25f,0f);
    }

    private static void renderParticles(DrawContext context, float elapsed, int x, int height) {
        float potionPosY = MathHelper.lerp((elapsed/(24f/30f)), 72f, 56f);
        float potionPosX = MathHelper.lerp((elapsed/(24f/30f)), 16f, 40f);
        float posTrans   = MathHelper.lerp((elapsed/(24f/30f)), 1f,  0f);

        RenderSystem.setShaderColor(1f, 1f, 1f, posTrans);
        context.drawTexture(PARTICLE, (x - 8) - (int) potionPosX, (((13 * height) / 50) - (int) ((34 * 2.8f) / 2)) + (int) potionPosY, 0, 0, 16, 16, 16, 16);
        context.drawTexture(PARTICLE, (x - 8) + (int) potionPosX, (((13 * height) / 50) - (int) ((34 * 2.8f) / 2)) + (int) potionPosY, 0, 0, 16, 16, 16, 16);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    private static void renderShield(DrawContext context, int x, int y) {
        float bigScale = 2.8f;
        float standardScale = 2.4f;

        if (elapsed < (8f/30f)){
            shieldScale = MathHelper.lerp((elapsed/(8f/30f)), shieldScale, bigScale);
        } else {
            shieldScale = MathHelper.lerp(((elapsed-(8f/30f))/(8f/30f)), shieldScale, standardScale);
        }

        context.drawTexture(SHIELD,     x, y,0,0,(int)(24* shieldScale),(int)(34* shieldScale),(int)(24* shieldScale),(int)(34* shieldScale));
        context.drawTexture(shield_icon,x, y,0,0,(int)(24* shieldScale),(int)(34* shieldScale),(int)(24* shieldScale),(int)(34* shieldScale));
    }

    public static void setIcon() {
        ServerInfoData.MINIGAME_TYPES Minigame = ServerInfoData.getMinigame();
        ServerInfoData.GAME_MODES Gamemode = ServerInfoData.getGamemode();
        ServerInfoData.MINIGAME_PHASES Phase = ServerInfoData.getPhase();
        if (Phase != SHOWDOWN) {
            if (Minigame == BATTLE) {
                shield_icon = Identifier.of("lemclienthelper", "textures/gui/battle/shield_icon.png");
            }
            if (Minigame == TUMBLE) {
                shield_icon = Identifier.of("lemclienthelper", "textures/gui/tumble/shield_icon.png");
            }
            if (Minigame == GLIDE) {
                if (Phase == COUNTDOWN) {
                    shield_icon = Identifier.of("lemclienthelper", "textures/gui/glide/shield_icon.png");
                } else {
                    shield_icon = Identifier.of("lemclienthelper", "textures/gui/" + (Gamemode == SCORE_ATTACK ? "stopwatch_icon" : "glide/score_icon") + ".png");
                }
            }
        } else {
            shield_icon = Identifier.of("lemclienthelper", "textures/gui/stopwatch_icon.png");
        }
    }

    public static void setElapsedMax(float max) {
        elapsedMax = max;
    }
}
