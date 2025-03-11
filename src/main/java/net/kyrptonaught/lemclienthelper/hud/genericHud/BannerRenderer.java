package net.kyrptonaught.lemclienthelper.hud.genericHud;

import net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.MINIGAME_TYPES.*;
import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.GAME_MODES.*;
import static net.kyrptonaught.lemclienthelper.ServerInfo.ServerInfoData.MINIGAME_PHASES.*;

public class BannerRenderer {
    //TODO Add these textures to assets.
    private static final Identifier SHIELD = Identifier.of("lemclienthelper","textures/gui/shield.png");
    private static Identifier shield_icon = Identifier.of("lemclienthelper","textures/gui/battle/shield_icon.png");

    public static void onHudRender(DrawContext context, RenderTickCounter v) {
       if (GenericHudMod.BANNER_RECEIVED) {renderBanner(context);}
    }

    public static void renderBanner(DrawContext context) {
        ServerInfoData.MINIGAME_TYPES Minigame = ServerInfoData.getMinigame();
        ServerInfoData.GAME_MODES Gamemode = ServerInfoData.getGamemode();
        ServerInfoData.MINIGAME_PHASES Phase = ServerInfoData.getPhase();

        if (Minigame == BATTLE) {shield_icon = Identifier.of("lemclienthelper","textures/gui/battle/shield_icon.png");}
        if (Minigame == TUMBLE) {shield_icon = Identifier.of("lemclienthelper","textures/gui/tumble/shield_icon.png");}
        if (Minigame == GLIDE) {shield_icon = Identifier.of("lemclienthelper","textures/gui/glide/" + ((Phase != COUNTDOWN) ? Gamemode == SCORE_ATTACK ? "stopwatch_icon":"score_icon" : "shield_icon") + ".png");}
        Text text = GenericHudMod.BANNER_TEXT;

        // Modify x to centre based on current matrix scale.
        int x = context.getScaledWindowWidth() / 2;

        /*
         * timer
         * shield matrix 0
         * banner matrix 0
         *
         * //Refine timing before implementing.
         * while timer < 1s
         *      shield matrix scale up
         * while timer > 1s <1.5s
         *      shield matrix scale down
         * while timer > 1.5s < 3s
         *      banner matrix scale up
         */
        GenericHudMod.BANNER_RECEIVED = false;
    }
}
