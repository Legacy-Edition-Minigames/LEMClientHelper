package net.kyrptonaught.lemclienthelper.hud.genericHud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class PlayerBarRenderer {

    private static final Identifier BIG_PLAYER_ICON_DEAD = Identifier.of("lem.base", "textures/playerbar/big/dead.png");
    private static final Identifier SMALL_PLAYER_ICON_DEAD = Identifier.of("lem.base", "textures/playerbar/small/dead.png");

    private static Identifier BIG_PLAYER_ICONS(int i) {
        if (i < 0 || i > 9) {
            throw new IllegalStateException("Attempted to fetch player bar icon index of: " + i + ", index must be between 0-9");
        } else {
            return Identifier.of("lem.base", "textures/playerbar/big/" + (i+1) + ".png");
        }
    }

    private static Identifier SMALL_PLAYER_ICONS(int i) {
        if (i < 0 || i > 15) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-15");
        } else {
            return Identifier.of("lem.base", "textures/playerbar/small/" + (i+1) + ".png");
        }
    }

    public static void renderPlayerBar(DrawContext context, RenderTickCounter v) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && HudMod.SHOULD_RENDER_PLAYERBAR && !client.options.hudHidden) {
            context.getMatrices().push();
            int l = context.getScaledWindowHeight() - 32 + 3;
            int x = context.getScaledWindowWidth() / 2 - 91;

            int p = HudMod.PLAYER_STATUS.length; // Amount of player icons
            int e;                               // Width of texture
            float u;                             // Width of texture + spacing.

            // Horrible awful, no good, very bad, spacing/centring code.
            if (p < 11) {
                e = 17;
                u = p > 8 ? 18f : 23f;
                if (HudMod.IN_ROUND) {
                    // If you're wondering about this calculation,
                    // This is the amount of pixels the matrix is shifted by
                    // after every icon, in order to evenly space all icons, when there are >8.
                    // I hope these calculations are so bad, somebody takes
                    // this code away from me, and completely rewrites it,
                    // so I never have to see it again.
                    u = p > 8 ? ((182f - (e * p))/(p - 1)) + e : (165f/7f);

                    context.getMatrices().translate(91f,0f,0f);                         // Shift start to centre of hotbar,
                    context.getMatrices().translate(((((p - 1) * u) + e)/-2f), 0f, 0f);    // Shift back by half of total playerbar width.
                }
            } else {
                e = 10;
                u = 11f;
                if (HudMod.IN_ROUND) {
                    u = ((182f - (e * p))/(p - 1))+ e;

                    context.getMatrices().translate(91f,0f,0f);
                    context.getMatrices().translate(((((p - 1) * u) + e)/-2f), 0f, 0f);
                }
            }

            RenderSystem.enableBlend();
            for (int i = 0; i < p; i++) {
                if (i > 0) {context.getMatrices().translate(u, 0f, 0f);}
                float[] c = RenderSystem.getShaderColor(); // Get the current shader colour to prevent breaking with hud transparency mods.
                if (!HudMod.IN_ROUND && HudMod.PLAYER_STATUS[i] == 0) {RenderSystem.setShaderColor(c[0],c[1],c[2], c[3]*(128F/255F));}
                if (p < 11) {
                    context.drawTexture(HudMod.PLAYER_STATUS[i] == 0 ? HudMod.IN_ROUND ? BIG_PLAYER_ICON_DEAD : BIG_PLAYER_ICONS(i) : BIG_PLAYER_ICONS(i), x, l, e, 5, e, 5, e, 5);
                } else {
                    context.drawTexture(HudMod.PLAYER_STATUS[i] == 0 ? HudMod.IN_ROUND ? SMALL_PLAYER_ICON_DEAD : SMALL_PLAYER_ICONS(i) : SMALL_PLAYER_ICONS(i),x, l, e, 5, e, 5, e, 5);
                }
                if (!HudMod.IN_ROUND && HudMod.PLAYER_STATUS[i] == 0) {RenderSystem.setShaderColor(c[0],c[1],c[2],c[3]*(255F/128F));}
            }
            RenderSystem.disableBlend();
            context.getMatrices().pop();
        }
    }
}
