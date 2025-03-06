package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * GlideScorePacket sets the clients current score.
 *
 * @param score    int, the total score the client has
 * @param lastRing int, the point value of the last ring collected, 0,    3,     5,      7
 *                                                                  grey, green, yellow, blue.
 */
public record GlideScorePacket(int score, int lastRing) implements CustomPayload {
    public static final Id<GlideScorePacket> PACKET_ID = new Id<>(Identifier.of("glidehud", "glide_score_set"));
    public static final PacketCodec<RegistryByteBuf, GlideScorePacket> codec = PacketCodec.tuple(
            PacketCodecs.INTEGER, GlideScorePacket::score,
            PacketCodecs.INTEGER, GlideScorePacket::lastRing,
            GlideScorePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
