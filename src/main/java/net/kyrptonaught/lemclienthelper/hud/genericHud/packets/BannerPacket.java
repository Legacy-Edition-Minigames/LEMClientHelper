package net.kyrptonaught.lemclienthelper.hud.genericHud.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * BannerPacket sends banner to client
 *
 * @param text Text, banner contents, such as "PLAYER won","Showdown!","0:00,000", etc.
 * @param elapsedMax float, length of time in seconds to keep the banner on screen
 */
public record BannerPacket(Text text, Optional<Float> elapsedMax) implements CustomPayload{
    public static final Id<BannerPacket> PACKET_ID = new Id<>(Identifier.of("hud", "banner"));
    public static final PacketCodec<RegistryByteBuf, BannerPacket> codec = PacketCodec.tuple(
            TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, BannerPacket::text,
            PacketCodecs.optional(PacketCodecs.FLOAT), BannerPacket::elapsedMax,
            BannerPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
