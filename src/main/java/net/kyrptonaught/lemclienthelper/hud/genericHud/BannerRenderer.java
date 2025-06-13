package net.kyrptonaught.lemclienthelper.hud.genericHud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.GAME_MODES.*;
import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.MINIGAME_PHASES.*;
import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.MINIGAME_TYPES.*;

public class BannerRenderer {
    private static final ResourceLocation SHIELD = ResourceLocation.fromNamespaceAndPath("lemclienthelper","textures/gui/shield.png");
    private static final ResourceLocation BANNER = ResourceLocation.fromNamespaceAndPath("lemclienthelper","textures/gui/banner.png");
    private static final ResourceLocation PARTICLE = ResourceLocation.fromNamespaceAndPath("minecraft","textures/particle/effect_4.png");
    private static final Minecraft client = Minecraft.getInstance();
    private static final Font textRenderer = client.font;

    private static float elapsed = 0.0f;
    private static float elapsedMax = 3.0f;

    private static float shieldScale = 0.0f;
    private static float bannerScale = 0.0f;
    private static float bannerWidth = 0.0f;
    private static float textTrans = 0.0f;

    private static ResourceLocation shield_icon = ResourceLocation.fromNamespaceAndPath("lemclienthelper","textures/gui/battle/shield_icon.png");

    public static void onHudRender(GuiGraphics context, DeltaTracker v) {
        if (GenericHudMod.BANNER_RECEIVED) {
            float lastDur = (v.getGameTimeDeltaTicks() / 20f);
            render(context, lastDur);
        }
    }

    public static void render(GuiGraphics context, float lastDur) {
        int width = client.getWindow().getGuiScaledWidth();
        int height = client.getWindow().getGuiScaledHeight();
        elapsed += lastDur;

        int x = (width/2) - (int)((24 * shieldScale)/2);
        int y = ((13 * height)/50) - (int)((34 * shieldScale)/2);

        context.pose().pushPose();
        RenderSystem.enableBlend();

        if (elapsed >= 0.1f) { renderParticles(context,elapsed,(width/2),height); }
        renderShield(context,x,y);
        if (elapsed >= (16f/30f)) { renderBanner(context,elapsed,width,height); }

        context.pose().popPose();

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

    private static void renderBanner(GuiGraphics context, float elapsed, int width, int height) {
        bannerScale = Mth.lerp((elapsed/(24f/30f)), bannerScale,2.4f);
        bannerWidth = Mth.lerp((elapsed/(24f/30f)), bannerWidth, (textRenderer.width(GenericHudMod.BANNER_TEXT)-((4*(int)bannerScale))));
        textTrans = Mth.lerp((elapsed/(40f/30f)),textTrans,1.0f);

        int x = (width/2) - (int)((bannerWidth)/2);
        int y = ((13 * height)/50) + (int)((7.5*((2*bannerScale)/2.4f)));

        context.blit(BANNER, (x-(7*(int)bannerScale)), y,(7*(int)(bannerScale)),8*(int)(bannerScale),0,0,7,8,31,8);
        context.blit(BANNER, (x),   y, (int) bannerWidth,8*(int)(bannerScale), 7*(int)(bannerScale),0, 1,8,31,8);
        context.blit(BANNER, ((width/2)+(int)((bannerWidth)/2)),   y,7*(int)(bannerScale),8*(int)(bannerScale),24,0,7,8,31,8);

        context.pose().translate(0f,0.25f,0f);
        RenderSystem.setShaderColor(1,1,1,textTrans);
        context.drawString(textRenderer,GenericHudMod.BANNER_TEXT,x-((2*(int)bannerScale)),y+2,0x444444,false);
        RenderSystem.setShaderColor(1,1,1,1);
        context.pose().translate(0f,-0.25f,0f);
    }

    private static void renderParticles(GuiGraphics context, float elapsed, int x, int height) {
        float potionPosY = Mth.lerp((elapsed/(24f/30f)), 72f, 56f);
        float potionPosX = Mth.lerp((elapsed/(24f/30f)), 16f, 40f);
        float posTrans   = Mth.lerp((elapsed/(24f/30f)), 1f,  0f);

        RenderSystem.setShaderColor(1f, 1f, 1f, posTrans);
        context.blit(PARTICLE, (x - 8) - (int) potionPosX, (((13 * height) / 50) - (int) ((34 * 2.8f) / 2)) + (int) potionPosY, 0, 0, 16, 16, 16, 16);
        context.blit(PARTICLE, (x - 8) + (int) potionPosX, (((13 * height) / 50) - (int) ((34 * 2.8f) / 2)) + (int) potionPosY, 0, 0, 16, 16, 16, 16);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    private static void renderShield(GuiGraphics context, int x, int y) {
        float bigScale = 2.8f;
        float standardScale = 2.4f;

        if (elapsed < (8f/30f)){
            shieldScale = Mth.lerp((elapsed/(8f/30f)), shieldScale, bigScale);
        } else {
            shieldScale = Mth.lerp(((elapsed-(8f/30f))/(8f/30f)), shieldScale, standardScale);
        }

        context.blit(SHIELD,     x, y,0,0,(int)(24* shieldScale),(int)(34* shieldScale),(int)(24* shieldScale),(int)(34* shieldScale));
        context.blit(shield_icon,x, y,0,0,(int)(24* shieldScale),(int)(34* shieldScale),(int)(24* shieldScale),(int)(34* shieldScale));
    }

    public static void setIcon() {
        ServerInfoData.MINIGAME_TYPES Minigame = ServerInfoData.getMinigame();
        ServerInfoData.GAME_MODES Gamemode = ServerInfoData.getGamemode();
        ServerInfoData.MINIGAME_PHASES Phase = ServerInfoData.getPhase();
        if (Phase != SHOWDOWN) {
            if (Minigame == BATTLE) {
                shield_icon = ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/battle/shield_icon.png");
            }
            if (Minigame == TUMBLE) {
                shield_icon = ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/tumble/shield_icon.png");
            }
            if (Minigame == GLIDE) {
                if (Phase == COUNTDOWN) {
                    shield_icon = ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/glide/shield_icon.png");
                } else {
                    shield_icon = ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/" + (Gamemode == SCORE_ATTACK ? "stopwatch_icon" : "glide/score_icon") + ".png");
                }
            }
        } else {
            shield_icon = ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/stopwatch_icon.png");
        }
    }

    public static void setElapsedMax(float max) {
        elapsedMax = max;
    }
}
