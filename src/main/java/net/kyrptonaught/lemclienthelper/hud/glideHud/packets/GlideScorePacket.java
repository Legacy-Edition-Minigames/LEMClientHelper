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
 * @param lastRing GlideScorePacket.rings, GREY, GREEN, YELLOW, BLUE, the last ring collected
 *
 */
public record GlideScorePacket(int score, rings lastRing) implements CustomPayload {
    public enum rings {
        GREY,
        GREEN,
        YELLOW,
        BLUE
    }
    public static final Id<GlideScorePacket> PACKET_ID = new Id<>(Identifier.of("glidehud", "glide_score_set"));
    public static final PacketCodec<RegistryByteBuf, GlideScorePacket> codec = PacketCodec.tuple(
            PacketCodecs.INTEGER, GlideScorePacket::score,
            PacketCodecs.indexed(i -> rings.values()[i], rings::ordinal), GlideScorePacket::lastRing,
            GlideScorePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
