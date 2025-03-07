package net.kyrptonaught.lemclienthelper.hud.genericHud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lemclienthelper.hud.HudMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class PlayerBarRenderer {

    // These names *MIGHT* be misleading, small player icons are the 1-8 player playerbar icons
    // while big player icons are the 9-16 player playerbar icons.
    // TODO: Fix this???????

    private static final Identifier SMALL_PLAYER_ICON_DEAD = Identifier.of("lemclienthelper", "textures/gui/generic/playerbar/small/dead.png");
    private static final Identifier BIG_PLAYER_ICON_DEAD = Identifier.of("lemclienthelper", "textures/gui/generic/playerbar/big/dead.png");


    //TODO, these are all colour shifted of the same icon, use 1 icon.
    private static Identifier SMALL_PLAYER_ICONS(int i) {
        if (i < 0 || i > 9) {
            throw new IllegalStateException("Attempted to fetch player bar icon index of: " + i + ", index must be between 0-9");
        } else {
            return Identifier.of("lemclienthelper", "textures/gui/generic/playerbar/small/" + i + ".png");
        }
    }

    private static Identifier SMALL_PLAYER_ICONS_NOT_READY(int i) {
        if (i < 0 || i > 9) {
            throw new IllegalStateException("Attempted to fetch player bar icon index of: " + i + ", index must be between 0-9");
        } else {
            return Identifier.of("lemclienthelper", "textures/gui/generic/playerbar/small/notready/" + i + ".png");
        }
    }

    private static Identifier BIG_PLAYER_ICONS(int i) {
        if (i < 0 || i > 15) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-15");
        } else {
            return Identifier.of("lemclienthelper", "textures/gui/generic/playerbar/big/" + i + ".png");
        }
    }

    private static Identifier BIG_PLAYER_ICONS_NOT_READY(int i) {
        if (i < 0 || i > 15) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-15");
        } else {
            return Identifier.of("lemclienthelper", "textures/gui/generic/playerbar/big/notready/" + i + ".png");
        }
    }

    public static void onHudRender(DrawContext context, RenderTickCounter v) {
        //TODO, check for mods like raised, l4j, lt, bedrockify, etc, anything that messes with the hotbar position,
        //      and shift the rendering position to match.
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
                    context.getMatrices().translate(((((p - 1) * u) + e)/-2f), 0f, 0f); // Shift back by half of total playerbar width.
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
                if (p < 11) {
                    if (HudMod.PLAYER_STATUS[i] == 0) {
                        context.drawTexture(HudMod.IN_ROUND ? SMALL_PLAYER_ICON_DEAD : SMALL_PLAYER_ICONS_NOT_READY(i), x, l, e, 5, e, 5, e, 5);
                    } else {
                        context.drawTexture(SMALL_PLAYER_ICONS(i), x, l, e, 5, e, 5, e, 5);
                    }
                } else {
                    if (HudMod.PLAYER_STATUS[i] == 0) {
                        context.drawTexture(HudMod.IN_ROUND ? BIG_PLAYER_ICON_DEAD : BIG_PLAYER_ICONS_NOT_READY(i),x, l, e, 5, e, 5, e, 5);
                    } else {
                        context.drawTexture(BIG_PLAYER_ICONS(i), x, l, e, 5, e, 5, e, 5);
                    }
                }

            }
            RenderSystem.disableBlend();
            context.getMatrices().pop();
        }
    }
}
