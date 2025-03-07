package net.kyrptonaught.lemclienthelper.hud.glideHud;

import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class GlideHudRenderer {
    private static final Identifier SPEEDOMETER = Identifier.of("lemclienthelper", "textures/gui/glide/speedometer/speedometer.png");
    private static final Identifier STOPWATCH = Identifier.of("lemclienthelper", "textures/gui/glide/timer/clock.png");

    private static Identifier SPEEDOMETER_NEEDLE(int i) {
        if (i < 0 || i > 11) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-11");
        } else {
            return Identifier.of("lemclienthelper", "textures/gui/glide/speedometer/needle_" + i + ".png");
        }
    }

    private static Identifier SECONDS_HAND(int i) {
        if (i < 0 || i > 11) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-11");
        } else {
            return Identifier.of("lemclienthelper", "textures/gui/glide/timer/seconds_" + i + ".png");
        }
    }

    private static Identifier MINUTES_HAND(int i) {
        if (i < 0 || i > 11) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-11");
        } else {
            return Identifier.of("lemclienthelper", "textures/gui/glide/timer/minutes_" + i + ".png");
        }
    }

    static int elapsed = 0;

    public static void onHudRender(DrawContext context, RenderTickCounter v) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && HudMod.shouldDisplayGlide() && !client.options.hudHidden) {
            int height = client.getWindow().getScaledHeight();
            int width = client.getWindow().getScaledWidth();

            context.getMatrices().push();
            context.getMatrices().translate((width - HudMod.getConfig().xOffset), height / 2f, 0);
            context.getMatrices().scale(HudMod.getConfig().armorHudScale, HudMod.getConfig().armorHudScale, 1f);
            context.getMatrices().translate(0, -24, 0);
            if (HudMod.GLIDE_SCORE_ATTACK) {context.getMatrices().translate(0, 24, 0);}
            context.setShaderColor(1f, 1f, 1f, HudMod.getConfig().transparency);
            renderStopwatch(context, client);
            renderSpeedometer(context, client);
            if (HudMod.GLIDE_SCORE_ATTACK) {renderScore(context, client);}
            context.setShaderColor(1f, 1f, 1f, 1f);
            context.getMatrices().pop();
        }
    }


    public static void renderStopwatch(DrawContext context, MinecraftClient client) {
        context.drawTexture(STOPWATCH, 0, -50,0,0,16,16,16,16);
        if (HudMod.TIMER_RUNNING) {
            elapsed = HudMod.ELAPSED_TIME;
            HudMod.FINAL_TIME = elapsed; //This prevents the time from displaying 0 if the final time packet is not received.
        } if (!HudMod.TIMER_RUNNING) {
            elapsed = HudMod.FINAL_TIME;
        }
        int seconds = (elapsed / 20) % 60;
        int minutes = (elapsed / 20) / 60;
        int milliseconds = (elapsed * 50) % 1000;
        int secondsindex = (seconds / 10) % 11;
        int minutesindex = (minutes / 5) % 11;

        context.drawTexture(SECONDS_HAND(secondsindex), 0, -50,0,0,16,16,16,16);
        context.drawTexture(MINUTES_HAND(minutesindex), 0, -50,0,0,16,16,16,16);

        String timer = String.format("%d:%02d.%03d", minutes, seconds, milliseconds);

        int textWidth = client.textRenderer.getWidth(timer);
        context.getMatrices().scale(1.25f,1.25f,1.25f);
        context.drawText(client.textRenderer, timer, (-4 - textWidth), -38, 0xffffff, true);
        context.getMatrices().scale(0.8f,0.8f,0.8f);
    }

    public static void renderSpeedometer(DrawContext context, MinecraftClient client) {
        double mps = client.player.getVelocity().horizontalLength() * 20;
        int needleIndex = MathHelper.clamp(MathHelper.floor(0.24 * mps - 0.5), 0, 11);
        context.drawTexture(SPEEDOMETER, 0, -30,0,0,16,16,16,16);
        context.drawTexture(SPEEDOMETER_NEEDLE(needleIndex), 0, -30,0,0,16,16, 16,16);

        String speed = String.format("%.2f m/s", mps);
        int textWidth = client.textRenderer.getWidth(speed);
        context.drawText(client.textRenderer, speed, (-4 - textWidth), (-30), 0xffffff, true);
    }

    public static void renderScore(DrawContext context, MinecraftClient client) {
        context.drawTexture(SPEEDOMETER, 0, -10,0,0,16,16,16,16);

        String score = String.valueOf(HudMod.GLIDE_SCORE);
        int textWidth = client.textRenderer.getWidth(score);
        context.drawText(client.textRenderer, score, (-4 - textWidth), (-30), 0xffffff, true);
    }
}
