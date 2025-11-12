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
            ServerInfoData.setPhase(payload.phase());
            ServerInfoData.setPlayerStatus(payload.playerStatus());
        }));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.minigamePacket.PACKET_ID, serverInfoPackets.minigamePacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.minigamePacket.PACKET_ID, ((payload, context) -> ServerInfoData.setMinigame(payload.minigame())));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.gamemodePacket.PACKET_ID, serverInfoPackets.gamemodePacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.gamemodePacket.PACKET_ID, ((payload, context) -> ServerInfoData.setGamemode(payload.gamemode())));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.phasePacket.PACKET_ID, serverInfoPackets.phasePacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.phasePacket.PACKET_ID, ((payload, context) -> ServerInfoData.setPhase(payload.phase())));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.playerStatusPacket.PACKET_ID, serverInfoPackets.playerStatusPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.serverInfoPacket.PACKET_ID, ((payload, context) -> ServerInfoData.setPlayerStatus(payload.playerStatus())));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.serverInfoIndicesPacket.PACKET_ID, serverInfoPackets.serverInfoIndicesPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.serverInfoIndicesPacket.PACKET_ID, ((payload, context) -> {
            ServerInfoData.setMinigame(ServerInfoData.MINIGAME_TYPES.values()[payload.minigame()]);
            ServerInfoData.setGamemode(ServerInfoData.GAME_MODES.values()[payload.gamemode()]);
            ServerInfoData.setPhase(ServerInfoData.MINIGAME_PHASES.values()[payload.phase()]);
            ServerInfoData.setPlayerStatus(payload.playerStatus());
        }));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.gamemodeIndexPacket.PACKET_ID, serverInfoPackets.gamemodeIndexPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.gamemodeIndexPacket.PACKET_ID, ((payload, context) -> ServerInfoData.setGamemode(ServerInfoData.GAME_MODES.values()[payload.gamemode()])));

        PayloadTypeRegistry.playS2C().register(serverInfoPackets.phaseIndexPacket.PACKET_ID, serverInfoPackets.phaseIndexPacket.codec);
        ClientPlayNetworking.registerGlobalReceiver(serverInfoPackets.phaseIndexPacket.PACKET_ID, ((payload, context) -> ServerInfoData.setPhase(ServerInfoData.MINIGAME_PHASES.values()[payload.phase()])));

    }
}
