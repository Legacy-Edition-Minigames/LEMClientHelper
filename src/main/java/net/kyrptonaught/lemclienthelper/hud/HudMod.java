package net.kyrptonaught.lemclienthelper.hud;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Identifier;

public class HudMod {
    private static final Identifier ARMOR_HUD_ENABLE = new Identifier("armorhud", "armor_hud_render_enable");
    private static final Identifier ARMOR_HUD_DISABLE = new Identifier("armorhud", "armor_hud_render_disable");

    public static boolean SHOULD_RENDER_ARMOR = false;

    public static void onInitialize() {
        //register hud's here
        HudRenderCallback.EVENT.register(ArmorHudRenderer::onHudRender);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_ARMOR = false);

        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_ENABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = true);
        ClientPlayNetworking.registerGlobalReceiver(ARMOR_HUD_DISABLE, (client, handler, buf, responseSender) -> SHOULD_RENDER_ARMOR = false);
    }
}
