package net.kyrptonaught.lemclienthelper.hud;

import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class GlideHudRenderer {
    private static final Identifier SPEEDOMETER = new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/speedometer.png");
    private static final Identifier[] ARROWS = new Identifier[]{
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/0.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/1.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/2.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/3.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/4.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/5.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/6.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/7.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/8.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/9.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/10.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/speedometer/arrow/11.png")
    };

    private static final Identifier STOPWATCH = new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/stopwatch.png");
    private static final Identifier[] SECONDS = new Identifier[]{
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/0.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/1.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/2.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/3.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/4.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/5.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/6.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/7.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/8.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/9.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/10.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/seconds/11.png")
    };
    private static final Identifier[] MINUTES = new Identifier[]{
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/0.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/1.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/2.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/3.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/4.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/5.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/6.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/7.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/8.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/9.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/10.png"),
        new Identifier(LEMClientHelperMod.MOD_ID, "textures/gui/stopwatch/minutes/11.png")
    };

    private static long elapsedTime = 0;
    private static long lastSecondsSpriteChange = 0;
    private static long lastMinutesSpriteChange = 0;
    private static int currentStopwatchSecondsSprite = 0;
    private static int currentStopwatchMinutesSprite = 0;

    public static void onHudRender(DrawContext context, float v) {
        renderSpeedometer(context, v);
        renderStopwatch(context, v);
    }

    public static double getPlayerSpeed() {
        MinecraftClient client = MinecraftClient.getInstance();
        var player = client.player;
        var pos = player.getPos();
        double dx = pos.x - player.lastRenderX;
        double dz = pos.z - player.lastRenderZ;
        double dy = pos.y - player.lastRenderY;
        /* 
           According to Skyeneko:
           The speedometer displays speed with the formula a^2 + b^2 = c^2 
           where a is vertical speed, b is horisontal speed, and c is the displayed speedometer speed.

           Thus, (dy * dy) is a^2
           (dx * dx) is the horisontal x speed^2
           (dz * dz) is the horisontal z speed^2
           (dx * dx + dz * dz) is the horisontal speed^2

           sqrt(a^2 + b^2) will give meters/tick
           sqrt(a^2 + b^2) * 20 will give the value for the speedometer in m/s
        */
        return Math.sqrt((dy * dy)+(dx * dx + dz * dz)) * 20;
    }

    public static int calculateArrow(double speed) {
        // I did refrence glide for this, they seem to change just before 5 at every increment
        double[] increments = {0.0, 4.8, 9.8, 14.8, 19.8, 24.8, 29.8, 34.8, 39.8, 44.8, 49.8};
        
        for (int i = 0; i < increments.length; i++) {
            if (speed < increments[i]) {
                return i;
            }
        }

        return increments.length;
    }

    public static void renderSpeedometer(DrawContext context, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && HudMod.shouldDisplayGlideHud()) {
            int height = client.getWindow().getScaledHeight();
            int width = client.getWindow().getScaledWidth();

            context.getMatrices().push();
            context.getMatrices().translate((width - (2 * HudMod.getConfig().xOffset)), height / 2f, 0);
            context.getMatrices().scale((HudMod.getConfig().glideHudScale * 0.75f), (HudMod.getConfig().glideHudScale * 0.75f), 1f);
            context.getMatrices().translate(0, 0, 0);

            context.drawTexture(SPEEDOMETER, 0, -96, 0, 0, 16, 16, 16, 16);

            double speed = getPlayerSpeed();

            int index = calculateArrow(speed);

            context.drawTexture(ARROWS[index], 0, -96, 0, 0, 16, 16, 16, 16);

            context.getMatrices().scale((0.75f), (0.75f), 1f);

            int textWidth = client.textRenderer.getWidth(String.format("%.2f", speed));
            int textOffset = -30 - textWidth;
            context.drawText(client.textRenderer, "m/s", -25, -122, 0xffffff, true);
            context.drawText(client.textRenderer, String.format("%.2f", speed), textOffset, -122, 0xffffff, true);

            context.getMatrices().pop();
        }
    }

    public static void incrementSecondsSprite() {
        currentStopwatchSecondsSprite = (currentStopwatchSecondsSprite + 1) % 11;
    }
    public static void incrementMinutesSprite() {
        currentStopwatchMinutesSprite = (currentStopwatchMinutesSprite + 1) % 11;
    }

    public static void incrementTimer(MinecraftClient client) {
        if (client.player != null && HudMod.isTimerRunning()) {
            elapsedTime = (System.nanoTime() - HudMod.getStartTime()); 
            if (System.nanoTime() - lastSecondsSpriteChange >= 5540000000l && elapsedTime >= 5540000000l) {
                incrementSecondsSprite();
                lastSecondsSpriteChange = System.nanoTime();
            }
            if (System.nanoTime() - lastMinutesSpriteChange >= 332400000000l && elapsedTime >= 332400000000l) {
                incrementMinutesSprite();
                lastMinutesSpriteChange = System.nanoTime();
            }
        } if ((client.player != null) && (HudMod.isTimerRunning() == false) && (HudMod.getFinalTime() != 0l)) {
            elapsedTime = HudMod.getFinalTime();
        }
    }

    public static int getStopwatchSecondsSprite() {
        return currentStopwatchSecondsSprite;
    }
    public static int getStopwatchMinutesSprite() {
        return currentStopwatchMinutesSprite;
    }

    public static void renderStopwatch(DrawContext context, float v) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && HudMod.shouldDisplayGlideHud()) {
            int height = client.getWindow().getScaledHeight();
            int width = client.getWindow().getScaledWidth();

            context.getMatrices().push();
            context.getMatrices().translate((width - (2 * HudMod.getConfig().xOffset)), height / 2f, 0);
            context.getMatrices().scale((HudMod.getConfig().glideHudScale * 0.75f), (HudMod.getConfig().glideHudScale * 0.75f), 1f);
            context.getMatrices().translate(0, -24, 0);

            context.drawTexture(STOPWATCH, 0, -96, 0, 0, 16, 16, 16, 16);

            int secondsIndex = getStopwatchSecondsSprite();
            int minutesIndex = getStopwatchMinutesSprite();

            context.drawTexture(SECONDS[secondsIndex], 0, -96, 0, 0, 16, 16, 16, 16);
            context.drawTexture(MINUTES[minutesIndex], 0, -96, 0, 0, 16, 16, 16, 16);

            long seconds = elapsedTime / 1000000000L;
            long minutes = seconds / 60;
            seconds %= 60;
            long milliseconds = (elapsedTime / 1000000L) % 1000;

            String timer = String.format("%d:%02d.%03d", minutes, seconds, milliseconds);

            int textWidth = client.textRenderer.getWidth(timer);
            int textOffset = -4 - textWidth;
            context.drawText(client.textRenderer, timer, textOffset, -92, 0xffffff, true);
           
            context.getMatrices().pop();
        }
    }
}
