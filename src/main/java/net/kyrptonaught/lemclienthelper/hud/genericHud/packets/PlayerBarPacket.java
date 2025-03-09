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
 */
public record PlayerBarPacket(boolean enabled) implements CustomPayload {
    public static final Id<PlayerBarPacket> PACKET_ID = new Id<>(Identifier.of("hud", "playerbar_enable"));
    public static final PacketCodec<RegistryByteBuf, PlayerBarPacket> codec = PacketCodec.tuple(
            PacketCodecs.BOOL, PlayerBarPacket::enabled,
            PlayerBarPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}