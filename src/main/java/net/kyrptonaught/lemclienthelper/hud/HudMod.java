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

    private static final Identifier SPEEDOMETER_HUD_ENABLE = new Identifier("speedometerhud", "speedometer_hud_render_enable");
    private static final Identifier SPEEDOMETER_HUD_DISABLE = new Identifier("speedometerhud", "speedometer_hud_render_disable");

    public static boolean SHOULD_RENDER_ARMOR = false;
    public static boolean SHOULD_RENDER_SPEEDOMETER = false;


    public static void onInitialize() {
        LEMClientHelperMod.configManager.registerFile(MOD_ID, new HudConfig());
        //register hud's here
        HudRenderCallback.EVENT.register(ArmorHudRenderer::onHudRender);
        HudRenderCallback.EVENT.register(SpeedometerHudRenderer::onHudRender);
        
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_ARMOR = false);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_SPEEDOMETER = false);

        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_ENABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = true);
        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_DISABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = false);
        ClientPlayNetworking.registerGlobalReceiver(SPEEDOMETER_HUD_ENABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_SPEEDOMETER = true);
        ClientPlayNetworking.registerGlobalReceiver(SPEEDOMETER_HUD_DISABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_SPEEDOMETER = false);
    }

    public static boolean shouldDisplay() {
        return getConfig().alwaysEnabled || SHOULD_RENDER_ARMOR;
    }

    public static boolean shouldDisplaySpeedometer() {
        return getConfig().speedometerAlwaysEnabled || SHOULD_RENDER_SPEEDOMETER;
    }

    public static HudConfig getConfig() {
        return (HudConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
