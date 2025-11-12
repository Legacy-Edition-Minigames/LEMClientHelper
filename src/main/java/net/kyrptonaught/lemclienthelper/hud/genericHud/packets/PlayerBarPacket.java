package net.kyrptonaught.lemclienthelper.hud.genericHud.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * PlayerBarPacket, send player bar information to client
 *
 * @param enabled boolean, is the playerbar enabled.
 */
public record PlayerBarPacket(boolean enabled) implements CustomPacketPayload {
    public static final Type<PlayerBarPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("hud", "playerbar_enable"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerBarPacket> codec = StreamCodec.composite(
            ByteBufCodecs.BOOL, PlayerBarPacket::enabled,
            PlayerBarPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}