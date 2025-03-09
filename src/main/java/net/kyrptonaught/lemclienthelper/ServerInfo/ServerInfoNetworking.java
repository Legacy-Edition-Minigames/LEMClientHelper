package net.kyrptonaught.lemclienthelper.ServerInfo;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.kyrptonaught.lemclienthelper.ServerInfo.packets.serverInfoPackets;

public class ServerInfoNetworking {
    public static void onInitialize() {
        PayloadTypeRegistry.playS2C().register(serverInfoPackets.serverInfoPacket.PACKET_ID, serverInfoPackets.serverInfoPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.serverInfoPacket.PACKET_ID, ((payload, context) -> {
            ServerInfoData.setMinigame(payload.minigame());
            ServerInfoData.setGamemode(payload.gamemode());
            ServerInfoData.setInRound(payload.inRound());
            ServerInfoData.setPlayerStatus(payload.playerStatus());
        }));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.minigamePacket.PACKET_ID, serverInfoPackets.minigamePacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.minigamePacket.PACKET_ID, ((payload, context) -> ServerInfoData.setMinigame(payload.minigame())));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.gamemodePacket.PACKET_ID, serverInfoPackets.gamemodePacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.gamemodePacket.PACKET_ID, ((payload, context) -> ServerInfoData.setGamemode(payload.gamemode())));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.inRoundPacket.PACKET_ID, serverInfoPackets.inRoundPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.inRoundPacket.PACKET_ID, ((payload, context) -> ServerInfoData.setInRound(payload.inRound())));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.playerStatusPacket.PACKET_ID, serverInfoPackets.playerStatusPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.serverInfoPacket.PACKET_ID, ((payload, context) -> ServerInfoData.setPlayerStatus(payload.playerStatus())));
        
    }
}
