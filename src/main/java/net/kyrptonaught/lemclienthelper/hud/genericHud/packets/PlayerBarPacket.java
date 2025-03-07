package net.kyrptonaught.lemclienthelper.hud.genericHud.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * PlayerBarPacket, send player bar information to client
 *
 * @param enabled boolean, is the playerbar enabled.
 * @param inRound boolean, is the client in a round.
 * @param players byte[],  0-1 for each player,
 *                         when in round: 0 - dead,      1 - alive,
 *                         when in lobby: 0 - not ready, 1- ready.
 */
public record PlayerBarPacket(boolean enabled, boolean inRound, byte[] players) implements CustomPayload {
    public static final Id<PlayerBarPacket> PACKET_ID = new Id<>(Identifier.of("hud", "playerbar_config"));
    public static final PacketCodec<RegistryByteBuf, PlayerBarPacket> codec = PacketCodec.tuple(
            PacketCodecs.BOOL, PlayerBarPacket::enabled,
            PacketCodecs.BOOL, PlayerBarPacket::inRound,
            PacketCodecs.byteArray(16), PlayerBarPacket::players,
            PlayerBarPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}