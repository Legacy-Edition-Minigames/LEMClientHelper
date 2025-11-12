package net.kyrptonaught.lemclienthelper.hud.armorHud;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.kyrptonaught.lemclienthelper.LEMClientHelperMod;
import net.kyrptonaught.lemclienthelper.hud.HudConfig;
import net.kyrptonaught.lemclienthelper.hud.armorHud.packets.ArmorHudPacket;

public class ArmorHudMod {
    public static String MOD_ID = "hud";

    public static boolean SHOULD_RENDER_ARMOR = false;

    public static void onInitialize() {
        HudRenderCallback.EVENT.register(ArmorHudRenderer::onHudRender);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_ARMOR = false);

        PayloadTypeRegistry.playS2C().register(ArmorHudPacket.PACKET_ID, ArmorHudPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(ArmorHudPacket.PACKET_ID, ((payload, context) -> SHOULD_RENDER_ARMOR = payload.enabled()));
    }

    public static boolean shouldDisplayArmor() {
        return getConfig().alwaysEnabled || SHOULD_RENDER_ARMOR;
    }


    public static HudConfig getConfig() {
        return (HudConfig) LEMClientHelperMod.configManager.getConfig(MOD_ID);
    }
}
