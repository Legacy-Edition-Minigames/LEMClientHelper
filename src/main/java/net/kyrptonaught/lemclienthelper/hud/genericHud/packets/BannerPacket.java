package net.kyrptonaught.lemclienthelper.hud.genericHud.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.Optional;

/**
 * BannerPacket sends banner to client
 *
 * @param text Text, banner contents, such as "PLAYER won","Showdown!","0:00,000", etc.
 * @param elapsedMax float, length of time in seconds to keep the banner on screen
 */
public record BannerPacket(Component text, Optional<Float> elapsedMax) implements CustomPacketPayload {
    public static final Type<BannerPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("hud", "banner"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BannerPacket> codec = StreamCodec.composite(
            ComponentSerialization.TRUSTED_STREAM_CODEC, BannerPacket::text,
            ByteBufCodecs.optional(ByteBufCodecs.FLOAT), BannerPacket::elapsedMax,
            BannerPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}
