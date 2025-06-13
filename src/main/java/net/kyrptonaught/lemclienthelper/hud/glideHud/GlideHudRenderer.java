package net.kyrptonaught.lemclienthelper.hud.glideHud;

import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GlideHudRenderer {
    private static final ResourceLocation SPEEDOMETER = ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/glide/speedometer/speedometer.png");
    private static final ResourceLocation STOPWATCH = ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/glide/timer/clock.png");

    private static ResourceLocation SPEEDOMETER_NEEDLE(int i) {
        if (i < 0 || i > 11) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-11");
        } else {
            return ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/glide/speedometer/needle_" + i + ".png");
        }
    }

    private static ResourceLocation SECONDS_HAND(int i) {
        if (i < 0 || i > 11) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-11");
        } else {
            return ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/glide/timer/seconds_" + i + ".png");
        }
    }

    private static ResourceLocation MINUTES_HAND(int i) {
        if (i < 0 || i > 11) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-11");
        } else {
            return ResourceLocation.fromNamespaceAndPath("lemclienthelper", "textures/gui/glide/timer/minutes_" + i + ".png");
        }
    }

    static int elapsed = 0;

    public static void onHudRender(GuiGraphics context, DeltaTracker v) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null && GlideHudMod.shouldDisplayGlide() && !client.options.hideGui) {
            int height = client.getWindow().getGuiScaledHeight();
            int width = client.getWindow().getGuiScaledWidth();

            context.pose().pushPose();
            context.pose().translate((width - HudMod.getConfig().xOffset), height / 2f, 0);
            context.pose().scale(HudMod.getConfig().armorHudScale, HudMod.getConfig().armorHudScale, 1f);
            context.pose().translate(0, -24, 0);
            if (ServerInfoData.getGamemode() == ServerInfoData.GAME_MODES.SCORE_ATTACK) {context.pose().translate(0, 24, 0);}
            context.setColor(1f, 1f, 1f, HudMod.getConfig().transparency);
            renderStopwatch(context, client);
            renderSpeedometer(context, client);
            if (ServerInfoData.getGamemode() == ServerInfoData.GAME_MODES.SCORE_ATTACK) {renderScore(context, client);}
            context.setColor(1f, 1f, 1f, 1f);
            context.pose().popPose();
        }
    }


    public static void renderStopwatch(GuiGraphics context, Minecraft client) {
        context.blit(STOPWATCH, 0, -50,0,0,16,16,16,16);
        if (GlideHudMod.TIMER_RUNNING) {
            elapsed = GlideHudMod.ELAPSED_TIME;
            GlideHudMod.FINAL_TIME = elapsed; //This prevents the time from displaying 0 if the final time packet is not received.
        } if (!GlideHudMod.TIMER_RUNNING) {
            elapsed = GlideHudMod.FINAL_TIME;
        }
        int seconds = (elapsed / 20) % 60;
        int minutes = (elapsed / 20) / 60;
        int milliseconds = (elapsed * 50) % 1000;
        int secondsindex = (seconds / 10) % 11;
        int minutesindex = (minutes / 5) % 11;

        context.blit(SECONDS_HAND(secondsindex), 0, -50,0,0,16,16,16,16);
        context.blit(MINUTES_HAND(minutesindex), 0, -50,0,0,16,16,16,16);

        String timer = String.format("%d:%02d.%03d", minutes, seconds, milliseconds);

        int textWidth = client.font.width(timer);
        context.pose().scale(1.25f,1.25f,1.25f);
        context.drawString(client.font, timer, (-4 - textWidth), -38, 0xffffff, true);
        context.pose().scale(0.8f,0.8f,0.8f);
    }

    public static void renderSpeedometer(GuiGraphics context, Minecraft client) {
        double mps = client.player.getDeltaMovement().horizontalDistance() * 20;
        int needleIndex = Mth.clamp(Mth.floor(0.24 * mps - 0.5), 0, 11);
        context.blit(SPEEDOMETER, 0, -30,0,0,16,16,16,16);
        context.blit(SPEEDOMETER_NEEDLE(needleIndex), 0, -30,0,0,16,16, 16,16);

        String speed = String.format("%.2f m/s", mps);
        int textWidth = client.font.width(speed);
        context.drawString(client.font, speed, (-4 - textWidth), (-30), 0xffffff, true);
    }

    public static void renderScore(GuiGraphics context, Minecraft client) {
        context.blit(SPEEDOMETER, 0, -10,0,0,16,16,16,16);

        String score = String.valueOf(GlideHudMod.GLIDE_SCORE);
        int textWidth = client.font.width(score);
        context.drawString(client.font, score, (-4 - textWidth), (-30), 0xffffff, true);
    }
}
