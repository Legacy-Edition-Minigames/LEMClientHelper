package net.kyrptonaught.lemclienthelper.hud;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.util.Identifier;

public class HudMod {
    public static String MOD_ID = "hud";

    private static final Identifier ARMOR_HUD_ENABLE = new Identifier("armorhud", "armor_hud_render_enable");
    private static final Identifier ARMOR_HUD_DISABLE = new Identifier("armorhud", "armor_hud_render_disable");

    private static final Identifier GLIDE_HUD_ENABLE = new Identifier("glidehud", "glide_hud_render_enable");
    private static final Identifier GLIDE_HUD_DISABLE = new Identifier("glidehud", "glide_hud_render_disable");

    private static final Identifier GLIDE_TIMER_START = new Identifier("glidehud","glide_timer_start");
    private static final Identifier GLIDE_TIMER_STOP = new Identifier("glidehud","glide_timer_stop");

    public static boolean SHOULD_RENDER_ARMOR = false;
    public static boolean SHOULD_RENDER_GLIDE_HUD = false;
    public static boolean GLIDE_TIMER_RUNNING = false;
    public static long GLIDE_TIMER_FINAL = 0l;
    public static long GLIDE_TIMER_START_TIME = 0l;


    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new HudConfig());
        ClientTickEvents.END_CLIENT_TICK.register(GlideHudRenderer::incrementTimer);
        //register hud's here
        HudRenderCallback.EVENT.register(ArmorHudRenderer::onHudRender);
        HudRenderCallback.EVENT.register(GlideHudRenderer::onHudRender);
        
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_ARMOR = false);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_GLIDE_HUD = false);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> GLIDE_TIMER_RUNNING = false);

        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_ENABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = true);
        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_DISABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = false);
        ClientPlayNetworking.registerGlobalReceiver(GLIDE_HUD_ENABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_GLIDE_HUD = true);
        ClientPlayNetworking.registerGlobalReceiver(GLIDE_HUD_DISABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_GLIDE_HUD = false);
        ClientPlayNetworking.registerGlobalReceiver(GLIDE_TIMER_START, (client, handler, buf, responseSender) -> { GLIDE_TIMER_RUNNING = true;  GLIDE_TIMER_START_TIME = System.nanoTime(); });
        ClientPlayNetworking.registerGlobalReceiver(GLIDE_TIMER_STOP, (client, handler, buf, responseSender) -> { GLIDE_TIMER_RUNNING = false; GLIDE_TIMER_FINAL = buf.readLong(); });
    }

    public static boolean shouldDisplay() {
        return getConfig().alwaysEnabled || SHOULD_RENDER_ARMOR;
    }

    public static boolean shouldDisplayGlideHud() {
        return getConfig().glideHudAlwaysEnabled || SHOULD_RENDER_GLIDE_HUD;
    }

    public static boolean isTimerRunning() {
        return GLIDE_TIMER_RUNNING;
    }

    public static long getStartTime() {
        return GLIDE_TIMER_START_TIME;
    }
    public static long getFinalTime() {
        return GLIDE_TIMER_FINAL;
    }

    public static HudConfig getConfig() {
        return (HudConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
