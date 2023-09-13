package net.kyrptonaught.lemclienthelper.hud;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.minecraft.util.Identifier;

public class HudMod {
    public static String MOD_ID = "hud";

    private static final Identifier ARMOR_HUD_ENABLE = new Identifier("armorhud", "armor_hud_render_enable");
    private static final Identifier ARMOR_HUD_DISABLE = new Identifier("armorhud", "armor_hud_render_disable");

    private static final Identifier GLIDE_HUD_ENABLE = new Identifier("glidehud", "glide_hud_render_enable");
    private static final Identifier GLIDE_HUD_DISABLE = new Identifier("glidehud", "glide_hud_render_disable");

    public static boolean SHOULD_RENDER_ARMOR = false;
    public static boolean SHOULD_RENDER_GLIDE_HUD = false;


    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new HudConfig());
        //register hud's here
        HudRenderCallback.EVENT.register(ArmorHudRenderer::onHudRender);
        HudRenderCallback.EVENT.register(GlideHudRenderer::onHudRender);
        
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_ARMOR = false);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_GLIDE_HUD = false);

        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_ENABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = true);
        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_DISABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = false);
        ClientPlayNetworking.registerGlobalReceiver(GLIDE_HUD_ENABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_GLIDE_HUD = true);
        ClientPlayNetworking.registerGlobalReceiver(GLIDE_HUD_DISABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_GLIDE_HUD = false);
    }

    public static boolean shouldDisplay() {
        return getConfig().alwaysEnabled || SHOULD_RENDER_ARMOR;
    }

    public static boolean shouldDisplayGlideHud() {
        return getConfig().glideHudAlwaysEnabled || SHOULD_RENDER_GLIDE_HUD;
    }

    public static HudConfig getConfig() {
        return (HudConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
