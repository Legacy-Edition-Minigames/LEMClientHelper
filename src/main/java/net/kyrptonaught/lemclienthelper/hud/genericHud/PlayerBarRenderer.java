package net.kyrptonaught.lemclienthelper.hud.genericHud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import wily.legacy.util.ScreenUtil;

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
            if (l4jLoaded) {ScreenUtil.prepareHUDRender(context);} else {context.pose().pushPose(); RenderSystem.enableBlend();}

            int hotbarHeight = (int)(32 * (3f/(l4jLoaded ? (float)ScreenUtil.getHUDScale(): 3f)));
            int hotbarWidth = (int)(91 * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f)));
            int l = context.guiHeight() - hotbarHeight + (int)(3 * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f)));
            int x = context.guiWidth() / 2 - hotbarWidth;

            byte[] s = ServerInfoData.getPlayerStatus();     // Array of player statuses
            int p = ServerInfoData.getPlayerStatus().length; // Amount of player icons
            int e;                                           // Width of texture
            float u;                                         // Width of texture + spacing.
            int h = (int)(5 * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f)));

            ServerInfoData.MINIGAME_PHASES Phase = ServerInfoData.getPhase();
            // Horrible awful, no good, very bad, spacing/centring code.
            if (p < 11) {
                e = (int)(17 * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f)));
                u = (p > 8 ? 18f : 23f) * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f));
                if (Phase != NONE) {
                    // If you're wondering about this calculation,
                    // This is the amount of pixels the matrix is shifted by
                    // after every icon, in order to evenly space all icons, when there are >8.
                    // I hope these calculations are so bad, somebody takes
                    // this code away from me, and completely rewrites it,
                    // so I never have to see it again.
                    u = p > 8 ? (((182f * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f))) - (e * p))/(p - 1)) + e : (165f/7f) * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f));

                    context.pose().translate((91f * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f))),0f,0f);                         // Shift start to centre of hotbar,
                    context.pose().translate(((((p - 1) * u) + e)/-2f), 0f, 0f);    // Shift back by half of total playerbar width.

                }
            } else {
                e = (int)(10 * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f)));
                u = 11f * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f));
                if (Phase != NONE) {
                    u = (((182f * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f))) - (e * p))/(p - 1))+ e;

                    context.pose().translate((91f * (3f/(l4jLoaded ? ScreenUtil.getHUDScale(): 3f))),0f,0f);
                    context.pose().translate(((((p - 1) * u) + e)/-2f), 0f, 0f);
                }
            }



            for (int i = 0; i < p; i++) {
                if (i > 0) {context.pose().translate(u, 0f, 0f);}
                float[] c = RenderSystem.getShaderColor(); // Get the current shader colour to prevent breaking with hud transparency mods.
                if (s[i] == 1 && l4jLoaded) {RenderSystem.setShaderColor(c[0],c[1],c[2], ScreenUtil.getHUDOpacity());}
                if ((Phase == NONE && s[i] == 0) || s[i] == -1) {RenderSystem.setShaderColor(c[0],c[1],c[2], (l4jLoaded ? ScreenUtil.getHUDOpacity() : c[3])*(128F/255F));}
                if (p < 11) {
                    context.blit(((s[i] == 0 || s[i] == -1) && (Phase != NONE)) ? BIG_PLAYER_ICON_DEAD : BIG_PLAYER_ICONS(i), x, l, e, h, e, h, e, h);
                } else {
                    context.blit(((s[i] == 0 || s[i] == -1) && (Phase != NONE)) ? SMALL_PLAYER_ICON_DEAD : SMALL_PLAYER_ICONS(i),x, l, e, h, e, h, e, h);
                }
                if ((Phase == NONE && s[i] == 0) || s[i] == -1) {RenderSystem.setShaderColor(c[0],c[1],c[2],(l4jLoaded ? ScreenUtil.getHUDOpacity() : c[3])*(255F/128F));}
                if (s[i] == 1 && l4jLoaded) {RenderSystem.setShaderColor(c[0],c[1],c[2], ScreenUtil.getHUDOpacity());}
            }

            if (l4jLoaded) {ScreenUtil.finalizeHUDRender(context);} else {RenderSystem.disableBlend(); context.pose().popPose();}

        }
    }
}
