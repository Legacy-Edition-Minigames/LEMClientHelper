package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.kyrptonaught.lemclienthelper.hud.glideHud.GlideHudMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * GlideScorePacket sets the clients current score.
 *
 * @param score    int, the total score the client has
 * @param lastRing {@link GlideHudMod.rings}, GREY, GREEN, YELLOW, BLUE, the last ring collected
 *
 */
public record GlideScorePacket(int score, GlideHudMod.rings lastRing) implements CustomPayload {
    public static final Id<GlideScorePacket> PACKET_ID = new Id<>(Identifier.of("glidehud", "glide_score_set"));
    public static final PacketCodec<RegistryByteBuf, GlideScorePacket> codec = PacketCodec.tuple(
            PacketCodecs.INTEGER, GlideScorePacket::score,
            PacketCodecs.indexed(i -> GlideHudMod.rings.values()[i], GlideHudMod.rings::ordinal), GlideScorePacket::lastRing,
            GlideScorePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }


    public record GlideScoreIndexPacket(int score, int lastRing) implements CustomPayload {
        public static final Id<GlideScoreIndexPacket> PACKET_ID = new Id<>(Identifier.of("glidehud", "glide_score_index_set"));
        public static final PacketCodec<RegistryByteBuf, GlideScoreIndexPacket> codec = PacketCodec.tuple(
                PacketCodecs.INTEGER, GlideScoreIndexPacket::score,
                PacketCodecs.INTEGER, GlideScoreIndexPacket::lastRing,
                GlideScoreIndexPacket::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}


