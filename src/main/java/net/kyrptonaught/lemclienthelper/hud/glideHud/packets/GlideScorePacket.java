package net.kyrptonaught.lemclienthelper.hud.glideHud.packets;

import net.kyrptonaught.lemclienthelper.hud.glideHud.GlideHudMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * GlideScorePacket sets the clients current score.
 *
 * @param score    int, the total score the client has
 * @param lastRing {@link GlideHudMod.rings}, GREY, GREEN, YELLOW, BLUE, the last ring collected
 *
 */
public record GlideScorePacket(int score, GlideHudMod.rings lastRing) implements CustomPacketPayload {
    public static final Type<GlideScorePacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("glidehud", "glide_score_set"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GlideScorePacket> codec = StreamCodec.composite(
            ByteBufCodecs.INT, GlideScorePacket::score,
            ByteBufCodecs.idMapper(i -> GlideHudMod.rings.values()[i], GlideHudMod.rings::ordinal), GlideScorePacket::lastRing,
            GlideScorePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }


    public record GlideScoreIndexPacket(int score, int lastRing) implements CustomPacketPayload {
        public static final Type<GlideScoreIndexPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("glidehud", "glide_score_index_set"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GlideScoreIndexPacket> codec = StreamCodec.composite(
                ByteBufCodecs.INT, GlideScoreIndexPacket::score,
                ByteBufCodecs.INT, GlideScoreIndexPacket::lastRing,
                GlideScoreIndexPacket::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}


