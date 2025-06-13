package net.kyrptonaught.lemclienthelper.TakeEverything;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TakeEverythingPacket(boolean enabled) implements CustomPacketPayload {
    public static final Type<TakeEverythingPacket> PACKET_ID = new Type<>(ResourceLocation.fromNamespaceAndPath("takeeverything", "take_everything_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TakeEverythingPacket> codec = ByteBufCodecs.BOOL.map(TakeEverythingPacket::new, TakeEverythingPacket::enabled).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

}
