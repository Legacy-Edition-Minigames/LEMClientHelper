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

    public static void onHudRender(DrawContext context, float v) {
        renderSpeedometer(context, v);
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
}
