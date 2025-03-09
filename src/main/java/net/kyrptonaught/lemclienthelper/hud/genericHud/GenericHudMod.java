package net.kyrptonaught.lemclienthelper.hud.genericHud;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.kyrptonaught.lemclienthelper.hud.genericHud.packets.BannerPacket;
import net.kyrptonaught.lemclienthelper.hud.genericHud.packets.PlayerBarPacket;
import net.minecraft.text.Text;

public class GenericHudMod {
    public static boolean SHOULD_RENDER_PLAYERBAR = false;

    public static boolean BANNER_RECEIVED = false; // Will set to false after banner is finished rendering.
    public static Text BANNER_TEXT = null;

    public static void onInitialize() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> SHOULD_RENDER_PLAYERBAR = false);

        PayloadTypeRegistry.playS2C().register(PlayerBarPacket.PACKET_ID, PlayerBarPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(PlayerBarPacket.PACKET_ID, ((payload, context) -> SHOULD_RENDER_PLAYERBAR = payload.enabled()));

        PayloadTypeRegistry.playS2C().register(BannerPacket.PACKET_ID, BannerPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(BannerPacket.PACKET_ID, ((payload, context) -> {
            BANNER_TEXT = payload.text();
            BANNER_RECEIVED = true;
        }));
    }
}
