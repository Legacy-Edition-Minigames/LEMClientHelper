package net.kyrptonaught.lemclienthelper.hud;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.util.Identifier;
import java.util.Arrays;

public class HudMod {
    public static String MOD_ID = "hud";

    private static final Identifier ARMOR_HUD_ENABLE = new Identifier("armorhud", "armor_hud_render_enable");
    private static final Identifier ARMOR_HUD_DISABLE = new Identifier("armorhud", "armor_hud_render_disable");

    public static boolean SHOULD_RENDER_ARMOR = false;
    public static float DMG_ANGLE = 0;
    public static int[] DMG_TIME_ANGLE = {100, 100, 100, 100, 100, 100, 100, 100};


    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new HudConfig());
        LEMClientHelperMod.configManager.load(MOD_ID);
        //register hud's here
        HudRenderCallback.EVENT.register(ArmorHudRenderer::onHudRender);
        HudRenderCallback.EVENT.register(DamageIndicatorHudRenderer::onHudRender);

        for (int i = 0; i < DMG_TIME_ANGLE.length; i++) {
            DMG_TIME_ANGLE[i] = getConfig().damageIndicatorFadeOut;
        }

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_ARMOR = false);

        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_ENABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = true);
        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_DISABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = false);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {onClientTick();});
    }

    public static void onClientTick() {
        for (int i = 0; i < DMG_TIME_ANGLE.length; i++) {
            if (DMG_TIME_ANGLE[i] < getConfig().damageIndicatorFadeOut) {
                DMG_TIME_ANGLE[i]++;
            }
        }
    }

    public static boolean shouldDisplay() {
        return getConfig().alwaysEnabled || SHOULD_RENDER_ARMOR;
    }

    public static boolean shouldDisplayDmgIndicator() {
        return getConfig().enableDamageIndicator;
    }

    public static float getDMGAngle() {
        return DMG_ANGLE;
    }

    public static void setDMGAngle(float angle) {
        DMG_ANGLE = angle;
    }

    public static void resetTimeForAngle(float angle) {
        if (Math.abs(angle) <= (45.0/2.0)) {
            DMG_TIME_ANGLE[1] = 0;
        }
        else if (Math.abs(angle) <= 45.0 + (45.0/2.0)) {
            if (angle < 0) {
                DMG_TIME_ANGLE[2] = 0;
            }
            else {
                DMG_TIME_ANGLE[0] = 0;
            }
        }
        else if (Math.abs(angle) <= 90.0 + (45.0/2.0)) {
            if (angle < 0) {
                DMG_TIME_ANGLE[4] = 0;
            }
            else {
                DMG_TIME_ANGLE[3] = 0;
            }
        }
        else if (Math.abs(angle) <= 180.0 - (45.0/2.0)) {
            if (angle < 0) {
                DMG_TIME_ANGLE[7] = 0;
            }
            else {
                DMG_TIME_ANGLE[5] = 0;
            }
        }
        else {
            DMG_TIME_ANGLE[6] = 0;
        }
    }

    public static int[] getDMGTimeAngle() {
        return Arrays.copyOf(DMG_TIME_ANGLE, DMG_TIME_ANGLE.length);
    }

    public static HudConfig getConfig() {
        return (HudConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
