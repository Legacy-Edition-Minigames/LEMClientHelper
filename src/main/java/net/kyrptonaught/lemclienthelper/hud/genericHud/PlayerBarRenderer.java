package net.kyrptonaught.lemclienthelper.hud.genericHud;

import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import wily.legacy.util.client.LegacyRenderUtil;

import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.MINIGAME_PHASES.*;

public class PlayerBarRenderer {
    private static final ResourceLocation BIG_PLAYER_ICON_DEAD = ResourceLocation.fromNamespaceAndPath("lem.base", "textures/playerbar/big/dead.png");
    private static final ResourceLocation SMALL_PLAYER_ICON_DEAD = ResourceLocation.fromNamespaceAndPath("lem.base", "textures/playerbar/small/dead.png");

    private static ResourceLocation BIG_PLAYER_ICONS(int i) {
        if (i < 0 || i > 9) {
            throw new IllegalStateException("Attempted to fetch player bar icon index of: " + i + ", index must be between 0-9");
        } else {
            return ResourceLocation.fromNamespaceAndPath("lem.base", "textures/playerbar/big/" + (i+1) + ".png");
        }
    }

    private static ResourceLocation SMALL_PLAYER_ICONS(int i) {
        if (i < 0 || i > 15) {
            throw new IllegalStateException("Attempted to fetch speedometer needle index of: " + i + ", index must be between 0-15");
        } else {
            return ResourceLocation.fromNamespaceAndPath("lem.base", "textures/playerbar/small/" + (i+1) + ".png");
        }
    }
    
    public static void renderPlayerBar(GuiGraphics context, DeltaTracker v){
        Minecraft client = Minecraft.getInstance();
        boolean l4jLoaded = FabricLoader.getInstance().isModLoaded("legacy");
        if (client.player != null && GenericHudMod.SHOULD_RENDER_PLAYERBAR && !client.options.hideGui) {
            if (l4jLoaded) {LegacyRenderUtil.prepareHUDRender(context);} else {context.pose().pushMatrix();}

            int hotbarHeight = (int)(32 * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale() : 3f)));
            int hotbarWidth = (int)(91 * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f)));
            int y = context.guiHeight() - hotbarHeight + (int)(3 * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f)));
            int x = context.guiWidth() / 2 - hotbarWidth;

            byte[] s = ServerInfoData.getPlayerStatus();              // Array of player statuses
            int p = ServerInfoData.getPlayerStatus().length;          // Amount of player icons
            int textureWidth;                                         // Width of texture
            float totalWidth;                                         // Width of texture + spacing.
            int textureHeight = (int)(5 * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f)));

            ServerInfoData.MINIGAME_PHASES Phase = ServerInfoData.getPhase();

            // Horrible awful, no good, very bad, spacing/centring code.
            if (p < 11) {
                textureWidth = (int)(17 * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f)));
                totalWidth = (p > 8 ? 18f : 23f) * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f));
                if (Phase != NONE) {
                    // If you're wondering about this calculation,
                    // This is the amount of pixels the matrix is shifted by
                    // after every icon, in order to evenly space all icons, when there are >8.
                    // I hope these calculations are so bad, somebody takes
                    // this code away from me, and completely rewrites it,
                    // so I never have to see it again.
                    totalWidth = p > 8 ? (((182f * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f))) - (textureWidth * p))/(p - 1)) + textureWidth : (165f/7f) * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f));

                    context.pose().translate((91f * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f))),0f);                         // Shift start to centre of hotbar,
                    context.pose().translate(((((p - 1) * totalWidth) + textureWidth)/-2f), 0f);    // Shift back by half of total playerbar width.

                }
            } else {
                textureWidth = (int)(10 * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f)));
                totalWidth = 11f * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f));
                if (Phase != NONE) {
                    totalWidth = (((182f * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f))) - (textureWidth * p))/(p - 1))+ textureWidth;

                    context.pose().translate((91f * (3f/(l4jLoaded ? LegacyRenderUtil.getHUDScale(): 3f))),0f);
                    context.pose().translate(((((p - 1) * totalWidth) + textureWidth)/-2f), 0f);
                }
            }


            for (int i = 0; i < p; i++) {
                if (i > 0) {context.pose().translate(totalWidth, 0f);}
                int c = ARGB.white(1f);

                if (s[i] == 1 && l4jLoaded) {c = ARGB.white(LegacyRenderUtil.getHUDOpacity());}
                if ((Phase == NONE && s[i] == 0) || s[i] == -1) {c = ARGB.white((l4jLoaded ? LegacyRenderUtil.getHUDOpacity() : 1f)*(128F/255F));}

                if (p < 11) {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, ((s[i] == 0 || s[i] == -1) && (Phase != NONE)) ? BIG_PLAYER_ICON_DEAD : BIG_PLAYER_ICONS(i), x, y, textureWidth, textureHeight, c);
                } else {
                    context.blitSprite(RenderPipelines.GUI_TEXTURED, ((s[i] == 0 || s[i] == -1) && (Phase != NONE)) ? SMALL_PLAYER_ICON_DEAD : SMALL_PLAYER_ICONS(i),x, y, textureWidth, textureHeight, c);
                }
            }

            if (l4jLoaded) { LegacyRenderUtil.finalizeHUDRender(context); } else { context.pose().popMatrix(); }

        }
    }
}
